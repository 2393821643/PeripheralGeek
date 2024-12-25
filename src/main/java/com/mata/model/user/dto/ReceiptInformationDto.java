package com.mata.model.user.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptInformationDto {
    // 收件电话
    @NotEmpty(message = "电话不能为空")
    private String phone;

    // 地址
    @NotEmpty(message = "收件不能为空")
    @Length(min = 1,max = 50,message = "地址长度大于0，小于50")
    private String address;

    // 收件人姓名
    @NotEmpty(message = "收件人不能为空")
    @Length(min = 1,max = 20,message = "收件人名字大于0，小于20")
    private String recipient;
}
