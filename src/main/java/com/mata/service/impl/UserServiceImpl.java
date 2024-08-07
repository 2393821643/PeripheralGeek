package com.mata.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.dao.UserDao;
import com.mata.dto.Result;
import com.mata.enumPackage.UserPositioning;
import com.mata.pojo.User;
import com.mata.service.UserService;
import com.mata.utils.RedisCommonKey;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    @Qualifier("userBloom")
    private RBloomFilter<Integer> userBloom;

    /**
     * 修改用户的个人信息
     */
    @Override
    public void updateUserMessage(User user) {
        updateById(user);
    }

    /**
     * 获取当前用户的个人信息 通过userid
     */
    @Override
    public Result<User> getUserInformationById(Integer userId, UserPositioning userPositioning) {
        // 如果定位是自己 直接数据库查
        if (userPositioning.equals(UserPositioning.My)) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(User::getAddress, User::getPhone, User::getRecipient, User::getUserId, User::getEmail, User::getSex, User::getUsername, User::getHeadUrl)
                    .eq(User::getUserId, userId);
            User user = getOne(wrapper);
            return Result.success(user);
        } else {
            // 如果定位是他人 先查redis 布隆过滤器 查看此人是否存在
            boolean contains = userBloom.contains(userId);
            if (!contains) {
                return Result.error("不存在此用户");
            }
            // 查redis缓存
            String userJson = stringRedisTemplate.opsForValue().get(RedisCommonKey.USER_ID_PRE_KEY+userId);
            if (userJson != null) {
                return Result.success(JSONUtil.toBean(userJson, User.class));
            }
            // 查数据库
            RLock lock = redissonClient.getLock(RedisCommonKey.USER_ID_LOCK_PRE_KEY+userId); // 创建锁对象
            try {
                boolean isLock = lock.tryLock(0, RedisCommonKey.USER_ID_LOCK_TIME, TimeUnit.SECONDS); //判断是否获取锁
                if (isLock) {
                    //获得到锁 数据库查用户信息
                    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
                    wrapper.select(User::getUserId, User::getEmail, User::getSex, User::getUsername, User::getHeadUrl)
                            .eq(User::getUserId, userId);
                    User resultUser = getOne(wrapper);
                    // 创建缓存
                    stringRedisTemplate.opsForValue().set(RedisCommonKey.USER_ID_PRE_KEY + userId, JSONUtil.toJsonStr(resultUser), RedisCommonKey.USER_ID_TIME, TimeUnit.MINUTES);
                    return Result.success(resultUser);
                } else {
                    // 没得到锁，递归重新查看缓存
                    return getUserInformationById(userId, UserPositioning.Other);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                boolean heldByCurrentThread = lock.isHeldByCurrentThread();
                if (heldByCurrentThread){
                    lock.unlock();
                }
            }
        }
    }


}
