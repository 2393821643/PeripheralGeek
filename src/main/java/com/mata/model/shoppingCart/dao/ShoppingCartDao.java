package com.mata.model.shoppingCart.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mata.model.shoppingCart.dto.ShoppingCartSearchDto;
import com.mata.model.shoppingCart.vo.ShoppingCartVo;
import com.mata.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShoppingCartDao extends BaseMapper<ShoppingCart> {
    /**
     * 获取购物车商品
     */
    IPage<ShoppingCartVo> getShoppingCartList(@Param("page") IPage<ShoppingCartVo> page, @Param("shoppingCartSearchDto") ShoppingCartSearchDto shoppingCartSearchDto);
}
