package com.mata.model.shoppingCart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartAddDto {
    private Long goodsId;

    private Integer goodsWillBuyCount;
}
