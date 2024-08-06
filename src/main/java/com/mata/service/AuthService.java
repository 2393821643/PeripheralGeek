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
     * 发送修改密码验证码 异步 发送到消息队列
     * @param email 发送验证码到此邮箱
     */
    Result sendChangePasswordCodeMessage(String email);

    /**
     * 发送修改密码验证码
     * @param email 发送验证码到此邮箱
     */
    void sendChangePasswordCode(String email);

    /**
     * 通过验证码登录
     * @param email 邮箱
     * @param code 验证码
     * @return token字符串
     */
    Result<String> loginByOpt(String email, String code);


    /**
     *  修改密码 异步 发送到消息队列
     * @param email 邮箱
     * @param code 验证码
     * @param password 修改的密码
     */
    Result changePasswordMessage(String email, String code,String password);

    /**
     * @param account  用户id/邮箱
     * @param password 密码
     * @return token字符串
     */
    Result<String> loginByPassword(String account, String password);
}
