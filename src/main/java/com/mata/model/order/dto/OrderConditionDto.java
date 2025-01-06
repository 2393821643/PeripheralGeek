package com.mata.model.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderConditionDto {
    // 当前页数
    private Integer pageNum;
    // 订单状态 查询条件 1：所有订单/2：未支付订单/3：待发货/4：已完成
    private Integer state;
    // 订单号
    private Long outTradeNo;
    // 用户id
    private Integer userId;
}
