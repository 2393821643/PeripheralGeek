package com.mata.model.shoppingCart.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartVo {
    private Integer id; // 购物车id

    @JsonSerialize(using = ToStringSerializer.class)
    private Long goodsId; // 商品id

    private Integer userId; // 用户id

    private Integer goodsWillBuyCount; // 将要购买的个数

    private String goodsName; // 商品名

    private Double goodsPrice; // 商品价格

    private String goodsUrl; // 商品图片url
}
