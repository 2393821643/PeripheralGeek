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
public class LoginByCodeDto {
    @Email(message = "请输入正确的邮箱")
    @NotBlank(message = "请输入正确的邮箱")
    private String email; // 邮箱


    @Length(max = 6,message = "验证码和邮箱不对应")
    private String code; //邮箱验证码
}
