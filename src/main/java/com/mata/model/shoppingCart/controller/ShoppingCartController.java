package com.mata.model.shoppingCart.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
import com.mata.model.shoppingCart.dto.ShoppingCartAddDto;
import com.mata.model.shoppingCart.dto.ShoppingCartSearchDto;
import com.mata.model.shoppingCart.service.ShoppingCartService;
import com.mata.model.shoppingCart.vo.ShoppingCartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 新增/修改购物车商品 返回购物车商品id
     */
    @PostMapping("/addGoods")
    @SaCheckLogin
    @SaCheckRole("user")
    public Result<Integer> addGoodsToShoppingCart(@RequestBody ShoppingCartAddDto shoppingCartAddDto){
        return shoppingCartService.addGoodsToShoppingCart(shoppingCartAddDto);
    }

    /**
     * 获取购物车列表
     */
    @PostMapping("/getShoppingCart")
    @SaCheckLogin
    @SaCheckRole("user")
    public Result<PageResult<ShoppingCartVo>> getShoppingCartList(@RequestBody ShoppingCartSearchDto shoppingCartSearchDto){
        return shoppingCartService.getShoppingCartList(shoppingCartSearchDto);
    }

    /**
     * 删除购物车商品
     */
    @DeleteMapping("/{shoppingCartId}")
    @SaCheckLogin
    @SaCheckRole("user")
    public Result deleteShoppingCart(@PathVariable("shoppingCartId") Integer shoppingCartId){
        return shoppingCartService.deleteShoppingCart(shoppingCartId);
    }

}
