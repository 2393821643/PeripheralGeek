package com.mata.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.EsDoc.GoodsDoc;
import com.mata.dao.GoodsDao;
import com.mata.dao.GoodsDocDao;
import com.mata.dto.GoodsAddDto;
import com.mata.dto.GoodsUpdateDto;
import com.mata.dto.Result;
import com.mata.enumPackage.CosFileMkdir;
import com.mata.pojo.Goods;
import com.mata.service.GoodsService;
import com.mata.utils.CosClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    @Qualifier("goodsBloom")
    private RBloomFilter<Long> goodsBloomFilter;

    /**
     * 管理员添加商品
     * 将商品图片和介绍先添加到COS，再发送消息队列异步添加到es和mysql
     */
    @Override
    public Result addGoods(GoodsAddDto goodsAddDto) {
        // 生成商品id
        Long goodsId = IdUtil.getSnowflakeNextId();
        // 加入布隆过滤器
        goodsBloomFilter.add(goodsId);
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
                return writeToCos(introductionBytes, IdUtil.getSnowflakeNextIdStr()+".html", CosFileMkdir.GoodsHtmlImg);
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
        // 发送信息队列
        rabbitTemplate.convertAndSend("GoodsExchange", "addGoodsKey", JSONUtil.toJsonStr(goods));
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
     * goods写入mysql
     */
    public void addGoodsToMysql(Goods goods) {
        save(goods);
    }

    /**
     * goods写入es
     */
    public void addGoodsToEs(Goods goods) {
        goodsDocDao.addGoods(new GoodsDoc(goods));
    }

    /**
     * 管理员删除商品
     * 发送信息队列，删除mysql和es的数据
     */
    @Override
    public Result deleteGoods(Long goodsId) {
        rabbitTemplate.convertAndSend("GoodsExchange", "deleteGoodsKey", goodsId.toString());
        return Result.success("删除成功");
    }

    /**
     * mysql删除商品
     */
    public void deleteGoodsToMysql(Long goodsId) {
        removeById(goodsId);
    }

    /**
     * ES删除商品
     */
    public void deleteGoodsToEs(String goodsId) {
        goodsDocDao.deleteGoods(goodsId);
    }


    /**
     * 管理员修改商品
     * 发送消息队列异步添加到es和mysql
     */
    @Override
    public Result updateGoods(GoodsUpdateDto goodsUpdateDto) {
        Goods goods = Goods.builder()
                .goodsId(goodsUpdateDto.getGoodsId())
                .goodsName(goodsUpdateDto.getGoodsName())
                .goodsBrand(goodsUpdateDto.getGoodsBrand())
                .goodsType(goodsUpdateDto.getGoodsType())
                .goodsConnectionType(goodsUpdateDto.getGoodsConnectionType())
                .goodsCount(goodsUpdateDto.getGoodsCount())
                .goodsPrice(goodsUpdateDto.getGoodsPrice())
                .build();
        String goodsJson = JSONUtil.toJsonStr(goods);
        rabbitTemplate.convertAndSend("GoodsExchange", "updateGoodsKey", goodsJson);
        return Result.success("修改成功");
    }

    /**
     * mysql修改商品
     */
    @Override
    public void updateGoodsToMysql(Goods goods) {
        updateById(goods);
    }

    /**
     * es修改商品
     */
    @Override
    public void updateGoodsToEs(Goods goods) {
        GoodsDoc goodsDoc = new GoodsDoc(goods);
        goodsDocDao.updateGoods(goodsDoc);
    }

    /**
     * 管理员修改图片
     * 发送消息队列异步添加到es和mysql
     */
    @Override
    public Result updateGoodsImg(Long goodsId, MultipartFile goodsImg) {
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
        Goods goods = Goods.builder()
                .goodsId(goodsId)
                .goodsUrl(imgUrl)
                .build();
        String goodsJson = JSONUtil.toJsonStr(goods);
        rabbitTemplate.convertAndSend("GoodsExchange","updateGoodsImgKey",goodsJson);
        return Result.success("修改图片成功");
    }

    /**
     * 更新商品的图片或介绍的地址到es
     */
    @Override
    public void updateGoodsFileToEs(Goods goods, CosFileMkdir cosFileMkdir) {
        goodsDocDao.updateGoodsFile(goods,cosFileMkdir);
    }

    /**
     * 管理员修改介绍
     * 发送消息队列异步添加到es和mysql
     */
    @Override
    public Result updateGoodsInformation(Long goodsId, String goodsIntroduction) {
        String imgInfomationUrl = null;
        try {
            // 写入cos
            byte[] imgBytes = goodsIntroduction.getBytes();
            // 返回cos的地址
            imgInfomationUrl = writeToCos(imgBytes, IdUtil.getSnowflakeNextIdStr()+".html", CosFileMkdir.GoodsHtmlImg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 组装对象
        Goods goods = Goods.builder()
                .goodsId(goodsId)
                .goodsIntroduction(imgInfomationUrl)
                .build();
        String goodsJson = JSONUtil.toJsonStr(goods);
        rabbitTemplate.convertAndSend("GoodsExchange","updateGoodsImgKey",goodsJson);
        return Result.success("修改商品介绍成功");
    }

    /**
     * 返回商品推荐词
     */
    @Override
    public Result<List<String>> getSuggestions(String goodsName) {
        List<String> suggestions = goodsDocDao.getSuggestions(goodsName);
        return Result.success(suggestions);
    }
}
