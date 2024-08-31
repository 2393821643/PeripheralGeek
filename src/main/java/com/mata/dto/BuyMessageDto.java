package com.mata.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 购买信息 如用户的收货地址，购买数量等
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BuyMessageDto {
    @Min(value = 1,message = "购买数量必须大于等于1")
    @NotNull(message = "购买数量不能为空")
    private Integer count; // 购买数量

    @NotEmpty(message = "手机号不能为空")
    @Length(min = 11,max = 11,message = "请输入正确的手机号")
    private String phone; // 收货手机号

    @NotEmpty(message = "地址不能为空")
    @Length(min = 1,max = 50,message = "地址长度要大于1，小于50")
    private String address; // 收货地址

    @NotEmpty(message = "收件人名称不能为空")
    @Length(min = 1,max = 30,message = "收件人名称长度要大于1，小于50")
    private String recipient; // 收件人名称
}
