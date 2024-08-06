package com.mata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.pojo.User;

public interface UserService extends IService<User> {
    void updateUserMessage(User user);
}
