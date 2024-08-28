package com.mata.controller;

import com.mata.dto.Result;
import com.mata.enumPackage.UserPositioning;
import com.mata.holder.Holder;
import com.mata.pojo.User;
import com.mata.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    public Result<User> getCurrentUserInformation(){
        Integer userId = Holder.getUser();
        return userService.getUserInformationById(userId, UserPositioning.My);
    }

    /**
     * 获取指定id用户的个人信息
     */
    @GetMapping("/information/other")
    public Result<User> getUserInformationById(@RequestParam("userId")
                                           @NotNull(message = "请输入要搜索用户的id") Integer userId){
        return userService.getUserInformationById(userId,UserPositioning.Other);
    }

    /**
     * 修改用户个人信息
     *
     */
    @PutMapping("/information")
    public Result updateUserInformationMessage(@RequestBody @Validated User user){
        return userService.updateUserInformationMessage(user);
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
    public Result<String> updateUserHeader(@RequestParam("img") MultipartFile img){
        return userService.updateUserHeader(img);
    }


}
