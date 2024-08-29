package com.mata.dao;

import com.mata.EsDoc.GoodsDoc;
import com.mata.enumPackage.CosFileMkdir;
import com.mata.pojo.Goods;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface GoodsDocDao {
    /**
     * 新增商品
     */
    void addGoods(GoodsDoc goodsDoc);

    /**
     * 删除商品
     */
    void deleteGoods(String goodsId);

    /**
     * 修改商品
     */
    void updateGoods(GoodsDoc goodsDoc);

    /**
     * 更新商品的图片或介绍的地址
     */
    void updateGoodsFile(Goods goods, CosFileMkdir cosFileMkdir);

    /**
     * 返回商品推荐词
     */
    List<String> getSuggestions(String goodsName);
}
