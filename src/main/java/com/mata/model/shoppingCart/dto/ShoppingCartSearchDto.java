package com.mata.model.shoppingCart.dto;

import lombok.Data;

@Data
public class ShoppingCartSearchDto {
    private Integer userId;

    private Integer pageNum;

    private String goodsName;
}
