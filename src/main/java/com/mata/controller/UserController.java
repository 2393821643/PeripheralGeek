package com.mata.controller;

import com.mata.dto.Result;
import com.mata.enumPackage.UserPositioning;
import com.mata.holder.Holder;
import com.mata.pojo.User;
import com.mata.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
     * 获取指定id用户的个人信息
     */
//    @GetMapping("/information/name")
//    public Result<User> getUserInformationByName(@RequestParam("username")
//                                           @NotEmpty(message = "请输入要搜索用户的用户名") String username){
//        return userService.getUserInformation(userId,UserPositioning.Other);
//    }
}
