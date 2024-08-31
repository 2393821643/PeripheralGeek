package com.mata.service.impl;

import cn.hutool.bloomfilter.BloomFilter;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.EsDoc.GoodsDoc;
import com.mata.dao.GoodsDao;
import com.mata.dao.OrderDao;
import com.mata.dao.UserDao;
import com.mata.dto.BuyMessageDto;
import com.mata.dto.Result;
import com.mata.holder.Holder;
import com.mata.pojo.Goods;
import com.mata.pojo.Order;
import com.mata.service.GoodsService;
import com.mata.service.OrderService;
import com.mata.service.UserService;
import com.mata.utils.AlipayUtil;
import com.mata.utils.RedisCommonKey;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, Order> implements OrderService {
    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private GoodsService goodsService;

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
                .build();
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
        return Result.success(orderHtml,null);
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
    }

    /**
     * 改变商品数量 数据库
     * 从缓存读数量 写入数据库
     */
    public void decreaseGoodsCount(Long goodsId) {
        // 获取修改数据库的锁
        RLock updateLock = redissonClient.getLock(RedisCommonKey.GOODS_COUNT_UPDATE_PRE_KEY+goodsId);
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
        System.out.println(signVerified);
        // 验证成功
        if (signVerified){
            // 获取out_trade_no
            String outTradeNo = map.get("out_trade_no");;
            Order order = Order.builder()
                    .outTradeNo(Long.valueOf(outTradeNo))
                    .state("已支付")
                    .build();
            this.updateOrder(order);
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
    public void updateOrder(Order order){
        updateById(order);
    }

}
