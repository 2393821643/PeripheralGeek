package com.mata.model.user.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.mata.model.user.dto.ReceiptInformationDto;
import com.mata.common.result.Result;
import com.mata.pojo.ReceiptInformation;
import com.mata.pojo.User;
import com.mata.model.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取当前用户的个人信息
     */
    @GetMapping("/information")
    @SaCheckLogin
    public Result<User> getCurrentUserInformation(){
        int userId = StpUtil.getLoginIdAsInt();
        return userService.getUserInformationById(userId);
    }

    /**
     * 修改用户个人信息
     *
     */
    @PutMapping("/information")
    @SaCheckLogin
    public Result updateUserInformationMessage(@RequestBody @Validated User user){
        return userService.updateUserInformationMessage(user);
    }

    /**
     * 设置用户默认收获信息
     */
    @PutMapping("/information/recipient")
    @SaCheckLogin
    public Result addOrUpdateReceiptInformation(@RequestBody ReceiptInformationDto receiptInformationDto){
        return userService.addOrUpdateReceiptInformation(receiptInformationDto);
    }

    /**
     * 获取用户默认收获信息
     */
    @GetMapping("/information/recipient")
    @SaCheckLogin
    public Result<ReceiptInformation> getReceiptInformation(){
        return userService.getReceiptInformation();
    }

    /**
     * 通过用户名查找用户
     */
    @GetMapping("/information/name")
    public Result<List<User>> getUserInformationByName(@RequestParam("username") String username){
        return userService.getUserInformationByName(username);
    }

    /**
     * 更新用户头像
     */
    @PutMapping("/header")
    @SaCheckLogin
    public Result<String> updateUserHeader(@RequestParam("img") MultipartFile img){
        return userService.updateUserHeader(img);
    }


}
