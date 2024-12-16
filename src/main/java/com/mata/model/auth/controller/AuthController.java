package com.mata.model.auth.controller;

import com.mata.common.result.Result;
import com.mata.model.auth.dto.ChangePasswordDto;
import com.mata.model.auth.dto.LoginByCodeDto;
import com.mata.model.auth.dto.LoginByPasswordDto;
import com.mata.model.auth.service.AuthService;
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
     * @return token字符串
     */
    @PostMapping("/user/login/otp")
    public Result<String> loginByOpt(@RequestBody @Validated LoginByCodeDto loginDto) {
        return authService.loginByOpt(loginDto.getEmail(), loginDto.getCode());
    }

    /**
     * 修改密码
     *
     */
    @PutMapping("/password")
    public Result changePassword(@RequestBody @Validated ChangePasswordDto authDto) {
        return authService.changePasswordMessage(authDto.getEmail(), authDto.getCode(), authDto.getPassword());
    }


    /**
     * 账号/邮箱 密码登录
     * @return token字符串
     */
    @PostMapping("/user/login")
    public Result<String> loginByPassword(@RequestBody @Validated LoginByPasswordDto loginDto) {
        return authService.loginByPassword(loginDto.getAccount(), loginDto.getPassword());
    }

    /**
     * 管理员登录
     */
    @PostMapping("/admin/login")
    public Result<String> adminLogin(@RequestBody @Validated LoginByPasswordDto loginDto) {
        return authService.adminLogin(loginDto.getAccount(),loginDto.getPassword());
    }
}
