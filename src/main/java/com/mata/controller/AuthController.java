package com.mata.controller;

import com.mata.dto.Result;
import com.mata.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {
    @Autowired
    private AuthService authService;

    /**
     * 发送登录验证码
     * @param email 发送验证码到此邮箱
     */
    @GetMapping("/code")
    public Result sendLoginCode(@RequestParam("email")
                                    @Email(message = "请输入正确的邮箱") String email){
        return authService.sendLoginCodeMessage(email);
    }

    /**
     * 通过验证码登录
     * @param email 邮箱
     * @param code 验证码
     * @return token字符串
     */
    @PostMapping("/user/login/otp")
    public Result<String> loginByOpt(@RequestParam("email") @Email(message = "请输入正确的邮箱") String email,
                                     @RequestParam("code") @NotNull(message = "请输入验证码") String code){
        return authService.loginByOpt(email,code);
    }
}
