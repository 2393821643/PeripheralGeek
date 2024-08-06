package com.mata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.dao.UserDao;
import com.mata.pojo.User;
import com.mata.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Override
    public void updateUserMessage(User user) {
        updateById(user);
    }
}
