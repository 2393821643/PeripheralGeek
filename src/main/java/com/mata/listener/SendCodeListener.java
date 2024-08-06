package com.mata.listener;

import cn.hutool.core.util.RandomUtil;
import com.mata.service.AuthService;
import com.mata.utils.EmailMessage;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendCodeListener {
    @Autowired
    private AuthService authService;

    /**
     * 发送登录验证码的队列
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "sendLoginCode"),
            exchange = @Exchange(name = "sendCodeExchange",type = ExchangeTypes.DIRECT),
            key = {"sendLoginCodeKey"}
    ))
    public void listenSendLoginCode(String email){
        authService.sendLoginCode(email);
    }

    /**
     * 发送修改密码验证码的队列
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "sendChangePasswordCode"),
            exchange = @Exchange(name = "sendCodeExchange",type = ExchangeTypes.DIRECT),
            key = {"sendChangePasswordCode"}
    ))
    public void listenSendChangePasswordCode(String email){
        authService.sendChangePasswordCode(email);
    }
}
