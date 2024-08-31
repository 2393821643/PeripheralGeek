package com.mata.utils;

public class RedisCommonKey {
    // 登录验证码前缀
    public final static String LOGIN_CODE_PER_KEY = "login:code:";

    // 登录验证码存活时间
    public final static long LOGIN_CODE_TIME = 2L;

    // 修改密码验证码前缀
    public final static String CHANGE_PASSWORD_CODE_PER_KEY = "change:code:";

    // 修改密码验证码存活时间
    public final static long CHANGE_PASSWORD_CODE_TIME = 2L;

    // 用户id缓存 前缀
    public final static String USER_ID_PRE_KEY = "user:id:";

    // 用户id缓存 存活时间
    public final static long USER_ID_TIME = 30L;


    // 创建用户缓存 锁前缀
    public final static String USER_ID_LOCK_PRE_KEY = "lock:user:id:";

    // 创建用户缓存 锁存活时间
    public final static long USER_ID_LOCK_TIME = 5L;

    // 创建用户名缓存 锁前缀
    public final static String USERNAME_LOCK_PRE_KEY = "lock:username:";

    // 创建用户名缓存 锁存活时间
    public final static long USERNAME_LOCK_TIME = 5L;

    // 创建用户名缓存 前缀
    public final static String USERNAME_KEY = "user:username:";

    // 创建用户名缓存 存活时间
    public final static long USERNAME_TIME = 5L;

    // 商品缓存键前缀
    public final static String GOODS_PRE_KEY = "goods:";

    // 商品缓存存活时间
    public final static long GOODS_TIME = 15L;

    // 商品缓存键
    public final static String GOODS_LOCK_PRE_KEY = "lock:goods:";

    // 商品缓存键存活时间
    public final static long GOODS_LOCK_TIME = 5L;

    // 商品数量缓存前缀
    public final static String GOODS_COUNT_PRE_KEY = "goods:count:";

    // 商品数量存活时间
    public final static long GOODS_COUNT_TIME = 15L;

    // 商品数量加锁前缀
    public final static String GOODS_COUNT_LOCK_PRE_KEY = "lock:goods:count:";

    // 商品数量加锁时间
    public final static long GOODS_COUNT_LOCK_TIME = 5L;

    // 商品修改锁前缀
    public final static String GOODS_COUNT_UPDATE_PRE_KEY="lock:goods:update:";

    // 商品修改锁时间
    public final static long GOODS_COUNT_UPDATE_TIME = 5L;
}
