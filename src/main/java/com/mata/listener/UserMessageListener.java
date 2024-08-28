package com.mata.listener;

import cn.hutool.json.JSONUtil;
import com.mata.pojo.User;
import com.mata.service.UserService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
        userService.updateUserInformation(user);
    }

    /**
     * 修改用户头像
     */
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "changeUserHeadImgQueue"),
//            exchange = @Exchange(name = "UserExchange",type = ExchangeTypes.DIRECT),
//            key = {"changeUserHeaderKey"}
//    ))
//    public void changeUserHeadImg(Message message){
//        userService.updateUserHeaderImg(message.getBody());
//    }

}
