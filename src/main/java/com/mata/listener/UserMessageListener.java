package com.mata.listener;

import cn.hutool.json.JSONUtil;
import com.mata.pojo.User;
import com.mata.service.UserService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMessageListener {
    @Autowired
    private UserService userService;

    /**
     * 修改用户信息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "changeUserQueue"),
            exchange = @Exchange(name = "UserExchange",type = ExchangeTypes.DIRECT),
            key = {"changeUserKey"}
    ))
    public void changeUser(String userJson){
        User user = JSONUtil.toBean(userJson, User.class);
        userService.updateUserMessage(user);
    }

    /**
     * 缓存用户信息
     */
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "cacheUserQueue"),
//            exchange = @Exchange(name = "UserExchange",type = ExchangeTypes.DIRECT),
//            key = {"cacheUserKey"}
//    ))
//    public void cacheUser(String userJson){
//        User user = JSONUtil.toBean(userJson, User.class);
//        userService.cacheUserInformation(user);
//    }
}
