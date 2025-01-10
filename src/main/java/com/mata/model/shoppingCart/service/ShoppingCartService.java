package com.mata.model.shoppingCart.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
import com.mata.model.shoppingCart.dto.ShoppingCartAddDto;
import com.mata.model.shoppingCart.dto.ShoppingCartSearchDto;
import com.mata.model.shoppingCart.vo.ShoppingCartVo;
import com.mata.pojo.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {
    /**
     * 新增购物车商品
     */
    Result<Integer> addGoodsToShoppingCart(ShoppingCartAddDto shoppingCartAddDto);

    /**
     * 获取购物车列表
     */
    Result<PageResult<ShoppingCartVo>> getShoppingCartList(ShoppingCartSearchDto shoppingCartSearchDto);

    /**
     * 删除购物车商品
     */
    Result deleteShoppingCart(Integer shoppingCartId);
}
