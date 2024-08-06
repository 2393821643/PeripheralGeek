package com.mata.controller;

import com.mata.dto.Result;
import com.mata.service.AuthService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {
    @Autowired
    private AuthService authService;

    /**
     * 发送登录验证码
     *
     * @param email 发送验证码到此邮箱
     */
    @GetMapping("/code")
    public Result sendLoginCode(@RequestParam("email") @Email(message = "请输入正确的邮箱") @NotBlank(message = "请输入正确的邮箱") String email) {
        return authService.sendLoginCodeMessage(email);
    }

    /**
     * 发送修改密码验证码
     *
     * @param email 发送验证码到此邮箱
     */
    @GetMapping("/password/code")
    public Result sendChangePasswordCode(@RequestParam("email") @Email(message = "请输入正确的邮箱") @NotBlank(message = "请输入正确的邮箱") String email) {
        return authService.sendChangePasswordCodeMessage(email);
    }

    /**
     * 通过验证码登录
     *
     * @param email 邮箱
     * @param code  验证码
     * @return token字符串
     */
    @PostMapping("/user/login/otp")
    public Result<String> loginByOpt(@RequestParam("email") @Email(message = "请输入正确的邮箱") @NotBlank(message = "请输入正确的邮箱") String email,
                                     @RequestParam("code") @NotBlank(message = "请输入验证码") String code) {
        return authService.loginByOpt(email, code);
    }

    /**
     *  修改密码
     * @param email 邮箱
     * @param code 验证码
     * @param password 修改的密码
     */
    @PutMapping("/password")
    public Result changePassword(@RequestParam("email") @Email(message = "请输入正确的邮箱") @NotBlank(message = "请输入正确的邮箱") String email,
                                 @RequestParam("code") @NotBlank(message = "请输入验证码") String code,
                                 @RequestParam("password") @Length(message = "密码长度要大于5，小于30",min = 5,max = 30) @NotBlank(message = "请输入密码") String password){
        return authService.changePasswordMessage(email,code,password);
    }


    /**
     * @param account  用户id/邮箱
     * @param password 密码
     * @return token字符串
     */
    @PostMapping("/user/login")
    public Result<String> loginByPassword(@RequestParam("account") @NotBlank(message = "请输入账号或邮箱") String account,
                                          @RequestParam("password") @NotBlank(message = "请输入密码") String password) {
        return authService.loginByPassword(account,password);
    }
}
