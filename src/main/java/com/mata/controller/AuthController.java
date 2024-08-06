package com.mata.controller;

import com.mata.dto.Result;
import com.mata.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;

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
}
