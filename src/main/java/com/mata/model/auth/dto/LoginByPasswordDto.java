package com.mata.model.auth.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LoginByPasswordDto {
    @NotBlank(message = "请输入邮箱或账号")
    private String account; // 账号/邮箱

    @Length(min = 5,max = 30,message = "密码错误")
    @NotBlank(message = "请输入密码")
    private String password; // 密码

}
