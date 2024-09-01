package com.mata.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.dao.GoodsDao;
import com.mata.dao.OrderDao;
import com.mata.dao.UserDao;
import com.mata.dto.BuyMessageDto;
import com.mata.dto.PageResult;
import com.mata.dto.Result;
import com.mata.holder.Holder;
import com.mata.pojo.Goods;
import com.mata.pojo.Order;
import com.mata.pojo.User;
import com.mata.service.GoodsService;
import com.mata.service.OrderService;
import com.mata.utils.AlipayUtil;
import com.mata.utils.EmailMessage;
import com.mata.utils.RedisCommonKey;
import com.mata.utils.SendEmailUtil;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, Order> implements OrderService {
    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    @Qualifier("goodsBloom")
    private RBloomFilter<Long> goodsBloomFilter;

    @Autowired
    private AlipayUtil alipayUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("orderBloom")
    private RBloomFilter<Long> orderBloomFilter;

    @Autowired
    private SendEmailUtil sendEmailUtil;


    /**
     * 购买商品，返回支付html
     */
    @Override
    public Result<String> buyGoods(Long goodsId, BuyMessageDto buyMessageDto) {
        // 查找商品数量
        Integer goodsCount = findGoodsCount(goodsId);
        if (goodsCount - buyMessageDto.getCount() < 0) {
            return Result.error("商品数量不足，购买失败");
        }
        // 查找商品信息
        Result<Goods> goodsResult = goodsService.getGoodsById(goodsId);
        Goods goods = goodsResult.getData();
        String goodsName = goods.getGoodsName();
        // 计算商品数量
        double goodsTotalPrice = goods.getGoodsPrice() * buyMessageDto.getCount();
        // 开始支付
        // 订单id
        String outTradeNo = IdUtil.getSnowflakeNextIdStr();
        // 加入订单bloom
        orderBloomFilter.add(Long.valueOf(outTradeNo));
        // 返回订单html
        String orderHtml = alipayUtil.createOrder(outTradeNo, BigDecimal.valueOf(goodsTotalPrice), goodsName);
        // 异步发送创建订单记录
        Order order = Order.builder()
                .outTradeNo(Long.valueOf(outTradeNo))
                .userId(Holder.getUser())
                .address(buyMessageDto.getAddress())
                .recipient(buyMessageDto.getRecipient())
                .phone(buyMessageDto.getPhone())
                .goodsName(goodsName)
                .state("未支付")
                .goodsId(goodsId)
                .goodsUrl(goods.getGoodsUrl())
                .createTime(LocalDateTime.now())
                .price(goodsTotalPrice)
                .goodsCount(buyMessageDto.getCount())
                .build();
        // 异步写入mysql
        rabbitTemplate.convertAndSend("OrderExchange", "createOrderKey", JSONUtil.toJsonStr(order));
        // 减少缓存商品数量
        decreaseCacheGoodsCount(buyMessageDto.getCount(), goodsId);
        // 发送消息队列，修改数据库商品数量，延迟队列
        Message message = MessageBuilder
                .withBody(goodsId.toString().getBytes(StandardCharsets.UTF_8)) //设置消息内容
                .setHeader("x-delay", 500000) // 设置消息延迟时间 5分钟
                .build();
        rabbitTemplate.convertAndSend("UpdateCountExchange", "updateCountKey", message);
        // 发布消息队列
        return Result.success(orderHtml, null);
    }

    /**
     * 查找商品数量
     */
    private Integer findGoodsCount(Long goodsId) {
        Integer count = 0;
        // 先查找商品是否存在
        boolean isExist = goodsBloomFilter.contains(goodsId);
        if (!isExist) {
            return 0;
        }

        // Redis查看商品数量
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("lock:goods:RWL");
        RLock rLock = readWriteLock.readLock();
        try {
            rLock.lock(15, TimeUnit.SECONDS);
            String countStr = stringRedisTemplate.opsForValue().get(RedisCommonKey.GOODS_COUNT_PRE_KEY + goodsId);
            // 如果字段存在
            if (!StrUtil.isEmpty(countStr)) {
                // 重置时间
                stringRedisTemplate.expire(RedisCommonKey.GOODS_COUNT_PRE_KEY + goodsId, RedisCommonKey.GOODS_COUNT_TIME, TimeUnit.MINUTES);
                count = Integer.valueOf(countStr);
                return count;
            }
        } finally {
            rLock.unlock();
        }

        // 不存在，创建缓存
        RLock lock = redissonClient.getLock(RedisCommonKey.GOODS_LOCK_PRE_KEY + goodsId);
        try {
            // 获得锁
            boolean isLock = lock.tryLock(0, RedisCommonKey.GOODS_LOCK_TIME, TimeUnit.SECONDS);
            if (isLock) {
                // 数据库查数量
                LambdaQueryWrapper<Goods> wapper = new LambdaQueryWrapper<>();
                wapper.select(Goods::getGoodsCount)
                        .eq(Goods::getGoodsId, goodsId);
                Goods goods = goodsDao.selectOne(wapper);
                count = goods.getGoodsCount();
                // 写入Redis
                stringRedisTemplate.opsForValue().set(RedisCommonKey.GOODS_COUNT_PRE_KEY + goodsId, count.toString(), RedisCommonKey.GOODS_COUNT_TIME, TimeUnit.MINUTES);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 解开锁
            boolean heldByCurrentThread = lock.isHeldByCurrentThread();
            if (heldByCurrentThread) {
                lock.unlock();
            }
        }
        return count;
    }

    /**
     * 减少商品缓存数量
     */
    private void decreaseCacheGoodsCount(Integer count, Long goodsId) {
        // 写锁
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("RWL");
        RLock rLock = readWriteLock.writeLock();
        try {
            rLock.lock(5, TimeUnit.SECONDS); // 加锁 锁的时间
            // 减少缓存商品数量
            stringRedisTemplate.opsForValue().increment(RedisCommonKey.GOODS_COUNT_PRE_KEY + goodsId, -count);
        } finally {
            rLock.unlock();
        }
    }


    /**
     * 订单写入数据库
     */
    public void createOrderToMysql(Order order) {
        save(order);
        // 同时写入Redis
        stringRedisTemplate.opsForValue().set(RedisCommonKey.ORDER_PRE_KEY + order.getOutTradeNo(), JSONUtil.toJsonStr(order), RedisCommonKey.ORDER_TIME, TimeUnit.MINUTES);
    }

    /**
     * 改变商品数量 数据库
     * 从缓存读数量 写入数据库
     */
    public void decreaseGoodsCount(Long goodsId) {
        // 获取修改数据库的锁
        RLock updateLock = redissonClient.getLock(RedisCommonKey.GOODS_COUNT_UPDATE_PRE_KEY + goodsId);
        try {
            boolean isLock = updateLock.tryLock(0, RedisCommonKey.GOODS_COUNT_UPDATE_TIME, TimeUnit.MINUTES);
            if (isLock) {
                // Redis查看商品数量
                // 加读锁
                RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("lock:goods:RWL");
                RLock rLock = readWriteLock.readLock();
                rLock.lock(15, TimeUnit.SECONDS);
                // redis读数据 写入数据库
                String countStr = stringRedisTemplate.opsForValue().get(RedisCommonKey.GOODS_COUNT_PRE_KEY + goodsId);
                Goods goods = Goods.builder()
                        .goodsId(goodsId)
                        .goodsCount(Integer.valueOf(countStr))
                        .build();
                goodsDao.updateById(goods);
                rLock.unlock();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 支付/退款回调接口
     */
    @Override
    public void payNotice(HttpServletRequest httpServletRequest) {
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        Map<String, String> map = convertMap(parameterMap);
        // 验证请求
        boolean signVerified = alipayUtil.verify(map);
        // 验证成功
        if (signVerified) {
            // 获取out_trade_no 和 goodsname
            String outTradeNo = map.get("out_trade_no");
            String goodsName = map.get("subject");
            String price = map.get("receipt_amount");
            Order order = Order.builder()
                    .outTradeNo(Long.valueOf(outTradeNo))
                    .goodsName(goodsName)
                    .price(Double.valueOf(price))
                    .state("已支付")
                    .build();
            // 修改数据库消息
            this.updateOrder(order);
            // 删除缓存
            stringRedisTemplate.delete(RedisCommonKey.ORDER_PRE_KEY + outTradeNo);
            // 重建缓存
            getOrderByOutTradeNo(Long.valueOf(outTradeNo));
            // 发送邮箱提示
            sendEmailPayMessage(order);
        }
    }

    /**
     * 将Map<String,String[]>转Map<String,String>，为payNotice这个函数服务
     */
    private Map<String, String> convertMap(Map<String, String[]> originalMap) {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String[]> entry : originalMap.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            // 假定数组总是有且仅有一个元素
            result.put(key, values[0]);
        }
        return result;
    }

    /**
     * 修改订单
     */
    public void updateOrder(Order order) {
        updateById(order);
    }

    /**
     * 发送邮箱 提示购买成功
     */
    private void sendEmailPayMessage(Order order) {
        // 数据库查找用户id
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Order::getUserId)
                .eq(Order::getOutTradeNo, order.getOutTradeNo());
        Integer userId = getOne(wrapper).getUserId();
        // 查找用户邮箱
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.select(User::getEmail).eq(User::getUserId, userId);
        String email = userDao.selectOne(userWrapper).getEmail();
        // 发送邮箱
        sendEmailUtil.sendEmail(email, EmailMessage.TITLE, EmailMessage.SEND_BUY_MESSAGE + order.getGoodsName() + EmailMessage.SEND_BUY_SUCCESS_MESSAGE + order.getOutTradeNo() +
                EmailMessage.SEND_BUY_SUCCESS_MESSAGE_PAY + order.getPrice() + "元");
    }

    /**
     * 继续支付
     */
    @Override
    public Result<String> continuePay(Long outTradeNo) {
        Order order = getOrderByOutTradeNo(outTradeNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        String orderHtml = alipayUtil.createOrder(order.getOutTradeNo().toString(), BigDecimal.valueOf(order.getPrice()), order.getGoodsName());
        return Result.success(orderHtml, null);
    }

    /**
     * 查看订单是否存在
     */
    private Order getOrderByOutTradeNo(Long outTradeNo) {
        Order order = null;
        // 查bloom 订单是否存在
        boolean contains = orderBloomFilter.contains(outTradeNo);
        if (!contains) {
            return null;
        }
        // 查redis
        String orderJson = stringRedisTemplate.opsForValue().get(RedisCommonKey.ORDER_PRE_KEY + outTradeNo);
        if (!StrUtil.isEmpty(orderJson)) {
            order = JSONUtil.toBean(orderJson, Order.class);
            return order;
        }
        // 查mysql
        order = getById(outTradeNo);
        if (order != null) {
            // 创建缓存
            stringRedisTemplate.opsForValue().set(RedisCommonKey.ORDER_PRE_KEY + order.getOutTradeNo(), JSONUtil.toJsonStr(order), RedisCommonKey.ORDER_TIME, TimeUnit.MINUTES);
            return order;
        }
        return null;
    }

    /**
     * 查看订单信息 只能看当前账号的的某个订单
     */
    @Override
    public Result<Order> getOrderMessage(Long outTradeNo) {
        Order order = getOrderByOutTradeNo(outTradeNo);
        if (order == null) {
            return Result.error("此订单不存在");
        }
        if (Objects.equals(order.getUserId(), Holder.getUser())) {
            return Result.success(order);
        }
        return Result.error("此订单不存在");
    }


    /**
     * 获取订单列表
     */
    @Override
    public Result<PageResult<Order>> getOrderPage(Integer page) {
        // 条件
        Page<Order> orderPage = lambdaQuery()
                .select(Order::getGoodsName, Order::getGoodsUrl, Order::getState, Order::getPrice, Order::getGoodsCount)
                .eq(Order::getUserId, Holder.getUser())
                .orderByDesc(Order::getCreateTime)
                .page(new Page<>(page, 20));
        // 装载数据
        PageResult<Order> orderResult = new PageResult<>();
        orderResult.setRecords(orderPage.getRecords());
        orderResult.setTotal(orderPage.getTotal());
        return Result.success(orderResult);
    }

    /**
     * 关闭交易
     */
    @Override
    public Result closeOrder(Long outTradeNo) {
        Order order = getOrderByOutTradeNo(outTradeNo);
        if (order == null) {
            return Result.error("此订单不存在");
        }
        if (!Objects.equals(order.getUserId(), Holder.getUser())) {
            return Result.error("此订单不存在");
        }
        // 获得订单状态
        String state = order.getState();
        if (!"未支付".equals(state)) {
            return Result.error("此订单已支付或关闭");
        }
        alipayUtil.closePay(outTradeNo.toString());
        // 删除缓存
        stringRedisTemplate.delete(RedisCommonKey.ORDER_PRE_KEY + outTradeNo);
        // 修改数据库状态
        order.setState("已关闭");
        rabbitTemplate.convertAndSend("OrderExchange", "updateOrderKey", JSONUtil.toJsonStr(order));
        return Result.success("交易已关闭");
    }

    /**
     * 管理员修改订单状态
     */
    @Override
    public Result updateOrderState(Long outTradeNo, String state) {
        // 获得订单信息
        Order order = getOrderByOutTradeNo(outTradeNo);
        if (order == null){
            return Result.error("此订单不存在");
        }
        order.setState(state);
        // 异步修改数据库
        rabbitTemplate.convertAndSend("OrderExchange", "updateOrderKey", JSONUtil.toJsonStr(order));
        return Result.success("修改成功");
    }
}
