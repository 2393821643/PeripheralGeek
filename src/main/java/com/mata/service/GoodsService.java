package com.mata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.dto.GoodsAddDto;
import com.mata.dto.GoodsUpdateDto;
import com.mata.dto.Result;
import com.mata.enumPackage.CosFileMkdir;
import com.mata.pojo.Goods;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GoodsService extends IService<Goods> {


    /**
     * 管理员添加商品
     * 将商品图片和介绍先添加到COS，再发送消息队列异步添加到es和mysql
     */
    Result addGoods(GoodsAddDto goodsAddDto);

    /**
     * goods写入mysql
     */
    void addGoodsToMysql(Goods goods);

    /**
     * goods写入es
     */
    void addGoodsToEs(Goods goods);

    /**
     * 管理员删除商品
     */
    Result deleteGoods(Long goodsId);

    /**
     * mysql删除商品
     */
    void deleteGoodsToMysql(Long goodsId);

    /**
     * ES删除商品
     */
    void deleteGoodsToEs(String goodsId);

    /**
     * 管理员修改商品
     * 发送消息队列异步添加到es和mysql
     */
    Result updateGoods(GoodsUpdateDto goodsUpdateDto);

    /**
     * mysql修改商品
     */
    void updateGoodsToMysql(Goods goods);

    /**
     * es修改商品
     */
    void updateGoodsToEs(Goods goods);

    /**
     * 管理员修改图片
     * 发送消息队列异步添加到es和mysql
     */
    Result updateGoodsImg(Long goodsId, MultipartFile goodsImg);


    /**
     * 修改商品的图片或介绍
     */
    void updateGoodsFileToEs(Goods goods, CosFileMkdir cosFileMkdir);

    /**
     * 管理员修改介绍
     * 发送消息队列异步添加到es和mysql
     */
    Result updateGoodsInformation(Long goodsId, String goodsIntroduction);

    /**
     * 返回商品推荐词
     */
    Result<List<String>> getSuggestions(String goodsName);
}
