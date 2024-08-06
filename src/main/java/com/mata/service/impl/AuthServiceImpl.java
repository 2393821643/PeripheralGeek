package com.mata.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.mata.dto.Result;
import com.mata.service.AuthService;
import com.mata.utils.EmailMessage;
import com.mata.utils.RedisCommonKey;
import com.mata.utils.SendEmailUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SendEmailUtil sendEmailUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发送登录验证码 异步 发送到消息队列
     * @param email 发送验证码到此邮箱
     */
    @Override
    public Result sendLoginCodeMessage(String email) {
        // 发送消息至消息队列
        rabbitTemplate.convertAndSend("sendCodeExchange","sendLoginCodeKey",email);
        // 返回成功信息
        return Result.success("发送成功");
    }

    /**
     * 发送登录验证码
     * @param email 发送验证码到此邮箱
     */
    @Override
    public void sendLoginCode(String email) {
        // 生成随机6位验证码
        String code = RandomUtil.randomNumbers(6);
        // 保存至Redis
        stringRedisTemplate.opsForValue().set(RedisCommonKey.LOGIN_CODE_PER_KEY+email,code,RedisCommonKey.LOGIN_CODE_TIME, TimeUnit.MINUTES);
        // 向此邮箱发送验证码
        sendEmailUtil.sendEmail(email,EmailMessage.TITLE,EmailMessage.SEND_LOGIN_CODE_MESSAGE_FOREBODY+code+EmailMessage.SEND_LOGIN_CODE_MESSAGE_BEHINDBODY);
    }


}
