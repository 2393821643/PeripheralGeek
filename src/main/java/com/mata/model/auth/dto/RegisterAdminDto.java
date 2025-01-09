package com.mata.model.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAdminDto {
    @Length(max = 30,message = "用户名长度不能超过30")
    @NotBlank(message = "请输入用户名")
    private String username;

    @Length(min = 5,max = 30,message = "密码长度大于5，小于30")
    @NotBlank(message = "请输入密码")
    private String password;
}
