package com.mata.utils;

public class RedisCommonKey {
    // 登录验证码前缀
    public final static String LOGIN_CODE_PER_KEY = "login:code:";
    // 登录验证码存活时间
    public final static long LOGIN_CODE_TIME = 2L;
}
