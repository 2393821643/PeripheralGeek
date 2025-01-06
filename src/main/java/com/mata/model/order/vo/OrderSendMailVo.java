package com.mata.model.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderSendMailVo {
    // 订单号
    public Long outTradeNo;
    // 商品名
    public String goodsName;
    // 邮箱
    public String email;
    // 快递编码
    public String courierCode;
}
