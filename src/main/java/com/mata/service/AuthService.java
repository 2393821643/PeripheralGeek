package com.mata.service;

import com.mata.dto.Result;

public interface AuthService {
    /**
     * 发送登录验证码 异步 发送到消息队列
     * @param email 发送验证码到此邮箱
     */
    Result sendLoginCodeMessage(String email);

    /**
     * 发送登录验证码
     * @param email 发送验证码到此邮箱
     */
    void sendLoginCode(String email);

    /**
     * 通过验证码登录
     * @param email 邮箱
     * @param code 验证码
     * @return token字符串
     */
    Result<String> loginByOpt(String email, String code);
}
