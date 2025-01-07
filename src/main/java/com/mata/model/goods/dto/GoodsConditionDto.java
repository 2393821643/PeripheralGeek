package com.mata.model.goods.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GoodsConditionDto {
    private Long goodsId; // 商品id

    private String goodsName; // 商品名

    private Integer pageNum; // 当前页
}
