package com.mata.model.goods.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.common.result.Suggest;
import com.mata.model.goods.dto.GoodsConditionDto;
import com.mata.model.goods.esDoc.GoodsDoc;
import com.mata.model.goods.dao.GoodsDao;
import com.mata.model.goods.esDao.GoodsDocDao;
import com.mata.model.goods.dto.GoodsAddDto;
import com.mata.model.goods.dto.GoodsUpdateDto;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
import com.mata.common.enumPackage.CosFileMkdir;
import com.mata.pojo.Goods;
import com.mata.model.goods.service.GoodsService;
import com.mata.utils.CosClientUtil;
import com.mata.common.redisKey.RedisCommonKey;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class GoodsServiceImpl extends ServiceImpl<GoodsDao, Goods> implements GoodsService {
    @Autowired
    private CosClientUtil cosClientUtil;

    @Autowired
    private GoodsDocDao goodsDocDao;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${file.path}")
    private String filePath;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 管理员添加商品
     * 将商品图片和介绍先添加到COS，再发送消息队列异步添加到es和mysql
     */
    @Override
    public Result addGoods(GoodsAddDto goodsAddDto) {
        // 生成商品id
        Long goodsId = IdUtil.getSnowflakeNextId();
        // 发送图片
        CompletableFuture<String> writeImg = CompletableFuture.supplyAsync(() -> {
            try {
                MultipartFile goodsUrlFile = goodsAddDto.getGoodsUrl();
                byte[] imgBytes = goodsUrlFile.getBytes();
                return writeToCos(imgBytes, goodsUrlFile.getOriginalFilename(), CosFileMkdir.GoodsImg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // 发送介绍html
        CompletableFuture<String> writeIntroduction = CompletableFuture.supplyAsync(() -> {
            try {
                byte[] introductionBytes = goodsAddDto.getGoodsIntroduction().getBytes();
                return writeToCos(introductionBytes, IdUtil.getSnowflakeNextIdStr() + ".html", CosFileMkdir.GoodsHtmlImg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        String goodsImgUrl = writeImg.join(); // 回调返回url
        String goodsIntroductionUrl = writeIntroduction.join(); // 回调返回url
        // 拼装原生Goods对象
        Goods goods = Goods.builder()
                .goodsId(goodsId)
                .goodsName(goodsAddDto.getGoodsName())
                .goodsBrand(goodsAddDto.getGoodsBrand())
                .goodsType(goodsAddDto.getGoodsType())
                .goodsConnectionType(goodsAddDto.getGoodsConnectionType())
                .goodsCount(goodsAddDto.getGoodsCount())
                .goodsUrl(goodsImgUrl)
                .goodsIntroduction(goodsIntroductionUrl)
                .goodsPrice(goodsAddDto.getGoodsPrice())
                .build();
        // 写入mysql
        save(goods);
        // 写入es
        goodsDocDao.addGoods(new GoodsDoc(goods));
        return Result.success("添加商品成功");
    }

    /**
     * 写入文件
     */
    private String writeToCos(byte[] fileByte, String fileName, CosFileMkdir cosFileMkdir) throws IOException {
        // 设置文件对象
        File convertedFile = new File(filePath + fileName);
        FileOutputStream fos = new FileOutputStream(convertedFile);
        // 写入本地文件
        fos.write(fileByte);
        fos.close();
        //写入cos
        String imgUrl = cosClientUtil.sendFile(convertedFile, cosFileMkdir);
        // 删除本地文件
        convertedFile.delete();
        return imgUrl;
    }

    /**
     * 管理员删除商品
     * 发送信息队列，删除mysql和es的数据
     */
    @Override
    public Result deleteGoods(Long goodsId) {
        rabbitTemplate.convertAndSend("GoodsExchange", "deleteGoodsKey", goodsId.toString());
        // 数据库删除
        removeById(goodsId);
        // 缓存删除
        stringRedisTemplate.delete(RedisCommonKey.GOODS_PRE_KEY + goodsId);
        // es删除
        goodsDocDao.deleteGoods(goodsId.toString());
        return Result.success("删除成功");
    }

    /**
     * 管理员修改商品
     * 发送消息队列异步添加到es和mysql
     */
    @Override
    public Result updateGoods(GoodsUpdateDto goodsUpdateDto) {
        // 查数据库
        Goods goods = getById(goodsUpdateDto.getGoodsId());
        goods.setGoodsName(goodsUpdateDto.getGoodsName());
        goods.setGoodsBrand(goodsUpdateDto.getGoodsBrand());
        goods.setGoodsType(goodsUpdateDto.getGoodsType());
        goods.setGoodsConnectionType(goodsUpdateDto.getGoodsConnectionType());
        goods.setGoodsCount(goodsUpdateDto.getGoodsCount());
        goods.setGoodsPrice(goodsUpdateDto.getGoodsPrice());
        updateGoodsToMysql(goods);
        updateGoodsToEs(goods);
        return Result.success("修改成功");
    }

    /**
     * mysql修改商品
     */
    private void updateGoodsToMysql(Goods goods) {
        updateById(goods);
        String goodsJson = JSONUtil.toJsonStr(goods);
        // 更新redis缓存
        stringRedisTemplate.opsForValue().set(RedisCommonKey.GOODS_PRE_KEY + goods.getGoodsId(), goodsJson, RedisCommonKey.GOODS_TIME, TimeUnit.MINUTES);
    }

    /**
     * es修改商品
     */
    private void updateGoodsToEs(Goods goods) {
        GoodsDoc goodsDoc = new GoodsDoc(goods);
        goodsDocDao.updateGoods(goodsDoc);
    }

    /**
     * 管理员修改图片
     * 发送消息队列异步添加到es和mysql
     */
    @Override
    public Result<String> updateGoodsImg(Long goodsId, MultipartFile goodsImg) {
        String imgUrl = null;
        try {
            // 写入cos
            byte[] imgBytes = goodsImg.getBytes();
            // 返回cos的地址
            imgUrl = writeToCos(imgBytes, goodsImg.getOriginalFilename(), CosFileMkdir.GoodsImg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 组装对象
        Goods goods = getById(goodsId);
        goods.setGoodsUrl(imgUrl);
        goodsDocDao.updateGoodsFile(goods, CosFileMkdir.GoodsImg);
        updateGoodsToMysql(goods);
        return Result.success(imgUrl,"修改图片成功");
    }

    /**
     * 管理员修改介绍
     * 发送消息队列异步添加到es和mysql
     */
    @Override
    public Result<String> updateGoodsInformation(Long goodsId, String goodsIntroduction) {
        String imgInfomationUrl = null;
        try {
            // 写入cos
            byte[] imgBytes = goodsIntroduction.getBytes();
            // 返回cos的地址
            imgInfomationUrl = writeToCos(imgBytes, IdUtil.getSnowflakeNextIdStr() + ".html", CosFileMkdir.GoodsHtmlImg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 组装对象
        Goods goods = getById(goodsId);
        goods.setGoodsIntroduction(imgInfomationUrl);
        goodsDocDao.updateGoodsFile(goods, CosFileMkdir.GoodsImg);
        updateGoodsToMysql(goods);
        goodsDocDao.updateGoodsFile(goods, CosFileMkdir.GoodsHtmlImg);
        return Result.success(imgInfomationUrl,"修改商品介绍成功");
    }

    /**
     * 返回商品推荐词
     */
    @Override
    public Result<List<Suggest>> getSuggestions(String goodsName) {
        List<String> suggestionStr = goodsDocDao.getSuggestions(goodsName);
        List<Suggest> suggestions = new ArrayList<>();
        suggestionStr.forEach(suggest -> suggestions.add(new Suggest(suggest)));
        return Result.success(suggestions);
    }

    /**
     * 搜索商品
     */
    @Override
    public Result<PageResult<Goods>> getGoodsByName(String goodsName, Integer page) {
        PageResult<Goods> goodsPageResult = goodsDocDao.getGoodsByName(goodsName, page);
        return Result.success(goodsPageResult);
    }


    /**
     * 搜索商品通过id
     */
    @Override
    public Result<Goods> getGoodsById(Long goodsId) {
        Goods resultGoods = null;
        // 查Redis缓存
        String goodsJson = stringRedisTemplate.opsForValue().get(RedisCommonKey.GOODS_PRE_KEY + goodsId);
        // 检查查出的缓存是否为空
        if (!StrUtil.isEmpty(goodsJson)) {
            resultGoods = JSONUtil.toBean(goodsJson, Goods.class);
            return Result.success(resultGoods);
        } else if ("".equals(goodsJson)) {
            return Result.error("此商品已下架或删除");
        }
        // 加锁创建缓存
        RLock lock = redissonClient.getLock(RedisCommonKey.GOODS_LOCK_PRE_KEY + goodsId); // 创建锁对象
        try {
            // 加锁
            boolean isLock = lock.tryLock(0, RedisCommonKey.GOODS_LOCK_TIME, TimeUnit.SECONDS);
            if (isLock) {
                // 查数据库
                resultGoods = getById(goodsId);
                if (resultGoods == null) {
                    stringRedisTemplate.opsForValue().set(RedisCommonKey.GOODS_PRE_KEY + goodsId, "", RedisCommonKey.GOODS_TIME, TimeUnit.MINUTES);
                    return Result.error("此商品已下架或删除");
                }
                // 创建缓存
                String toGoodsJson = JSONUtil.toJsonStr(resultGoods);
                stringRedisTemplate.opsForValue().set(RedisCommonKey.GOODS_PRE_KEY + goodsId, toGoodsJson, RedisCommonKey.GOODS_TIME, TimeUnit.MINUTES);
            } else {
                Thread.sleep(50);
                getGoodsById(goodsId);
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
        return Result.success(resultGoods);
    }

    /**
     * 管理员获取商品列表
     */
    @Override
    public Result<PageResult<Goods>> getGoodsToAdmin(GoodsConditionDto goodsConditionDto) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        // id/商品名不为空则添加条件，否则全查
        if (!StrUtil.isEmpty(goodsConditionDto.getGoodsName())) {
            wrapper.like(Goods::getGoodsName, goodsConditionDto.getGoodsName());
        }
        if (goodsConditionDto.getGoodsId() != null) {
            wrapper.eq(Goods::getGoodsId,goodsConditionDto.getGoodsId());
        }
        Page<Goods> page = Page.of(goodsConditionDto.getPageNum(), 20);
        Page<Goods> resultGoods = this.page(page, wrapper);
        PageResult<Goods> goodsPageResult = new PageResult<>(resultGoods.getTotal(),resultGoods.getRecords());
        return Result.success(goodsPageResult);
    }
}
