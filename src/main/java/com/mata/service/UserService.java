package com.mata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.dto.Result;
import com.mata.enumPackage.UserPositioning;
import com.mata.pojo.User;

public interface UserService extends IService<User> {
    /**
     * 修改用户信息
     */
    void updateUserMessage(User user);

    /**
     * 获取某个用户的个人信息 通过userid
     */
    Result<User> getUserInformationById(Integer userId, UserPositioning userPositioning);

    /**
     * 缓存用户信息
     */
   // public void cacheUserInformation(User user);
}
