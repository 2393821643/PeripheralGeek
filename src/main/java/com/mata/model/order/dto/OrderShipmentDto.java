package com.mata.model.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderShipmentDto {
    @NotNull(message = "订单号不能为空")
    // 订单号
    public Long outTradeNo;

    // 快递编码
    @NotEmpty(message = "快递单号不能为空")
    @Length(min = 1,max = 40,message = "快递单号长度不超过40")
    private String courierCode;
}
