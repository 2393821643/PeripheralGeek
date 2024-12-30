package com.mata.model.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.common.result.Suggest;
import com.mata.model.goods.dto.GoodsAddDto;
import com.mata.model.goods.dto.GoodsUpdateDto;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
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
     * 管理员删除商品
     */
    Result deleteGoods(Long goodsId);



    /**
     * 管理员修改商品
     * 发送消息队列异步添加到es和mysql
     */
    Result updateGoods(GoodsUpdateDto goodsUpdateDto);

    /**
     * 管理员修改图片
     * 发送消息队列异步添加到es和mysql
     */
    Result updateGoodsImg(Long goodsId, MultipartFile goodsImg);


    /**
     * 管理员修改介绍
     * 发送消息队列异步添加到es和mysql
     */
    Result updateGoodsInformation(Long goodsId, String goodsIntroduction);

    /**
     * 返回商品推荐词
     */
    Result<List<Suggest>> getSuggestions(String goodsName);

    /**
     * 搜索商品
     */
    Result<PageResult<Goods>> getGoodsByName(String goodsName, Integer page);

    /**
     * 搜索商品 通过id
     */
    Result<Goods> getGoodsById(Long goodsId);
}
