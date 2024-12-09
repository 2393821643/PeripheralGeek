package com.mata.model.goods.esDao;

import com.mata.model.goods.esDoc.GoodsDoc;
import com.mata.common.result.PageResult;
import com.mata.common.enumPackage.CosFileMkdir;
import com.mata.pojo.Goods;

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

    /**
     * 搜索商品
     */
    PageResult<Goods> getGoodsByName(String goodsName, Integer page);
}
