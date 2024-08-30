package com.mata.service.impl;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.dao.UserDao;
import com.mata.dto.Result;
import com.mata.enumPackage.CosFileMkdir;
import com.mata.enumPackage.UserPositioning;
import com.mata.holder.Holder;
import com.mata.pojo.User;
import com.mata.service.UserService;
import com.mata.utils.CosClientUtil;
import com.mata.utils.RedisCommonKey;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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

    @Autowired
    private CosClientUtil cosClientUtil;

    @Value("${file.path}")
    private String filePath;

    /**
     * 修改用户的个人信息
     */
    @Override
    public void updateUserInformation(User user) {
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
            wrapper.select(User::getAddress, User::getPhone, User::getRecipient, User::getUserId, User::getEmail, User::getSex, User::getUsername, User::getHeadUrl,User::getSign)
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
                    wrapper.select(User::getUserId, User::getEmail, User::getSex, User::getUsername, User::getHeadUrl,User::getSign)
                            .eq(User::getUserId, userId);
                    User resultUser = getOne(wrapper);
                    // 创建缓存
                    stringRedisTemplate.opsForValue().set(RedisCommonKey.USER_ID_PRE_KEY + userId, JSONUtil.toJsonStr(resultUser), RedisCommonKey.USER_ID_TIME, TimeUnit.MINUTES);
                    return Result.success(resultUser);
                } else {
                    // 没得到锁，递归重新查看缓存
                    Thread.sleep(50);
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

    /**
     * 修改用户信息 发送消息队列
     */
    @Override
    public Result updateUserInformationMessage(User user) {
        // 注入userId
        Integer userid = Holder.getUser();
        user.setUserId(userid);
        // 验证用户信息 手机号
        if (!StrUtil.isEmpty(user.getPhone()) && !PhoneUtil.isPhone(user.getPhone())){
            return Result.error("修改失败,手机号错误");
        }
        // 发送消息队列
        rabbitTemplate.convertAndSend("UserExchange","changeUserKey",JSONUtil.toJsonStr(user));
        return Result.success("修改成功");
    }

    /**
     * 通过用户名查找用户
     */
    @Override
    public Result<List<User>> getUserInformationByName(String username) {
        // 查缓存
        String usernameListJson = stringRedisTemplate.opsForValue().get(RedisCommonKey.USERNAME_KEY + username);
        // 查到缓存返回
        if (usernameListJson != null){
            List<User> userList = JSONUtil.toList(usernameListJson,User.class);
            return Result.success(userList);
        }
        // 没查到建立锁 查数据库
        RLock lock = redissonClient.getLock(RedisCommonKey.USERNAME_LOCK_PRE_KEY+username); // 创建锁对象
        try {
            // 是否获得锁
            boolean isLock = lock.tryLock(0, RedisCommonKey.USERNAME_LOCK_TIME, TimeUnit.SECONDS);
            if (isLock){
                // 查数据库
                LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
                wrapper.select(User::getUserId, User::getEmail, User::getSex, User::getUsername, User::getHeadUrl,User::getSign)
                        .likeRight(User::getUsername,username);
                List<User> userList = list(wrapper);
                // 建立缓存
                stringRedisTemplate.opsForValue().set(RedisCommonKey.USERNAME_KEY + username,JSONUtil.toJsonStr(userList),RedisCommonKey.USERNAME_TIME,TimeUnit.MINUTES);
                return Result.success(userList);
            }else {
                // 没获得锁 递归查看缓存
                Thread.sleep(50);
                return getUserInformationByName(username);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            // 解开锁
            boolean heldByCurrentThread = lock.isHeldByCurrentThread();
            if (heldByCurrentThread){
                lock.unlock();
            }
        }

    }

    /**
     * 修改用户头像
     */
    @Override
    public Result<String> updateUserHeader(MultipartFile img) {
        // 图片写入本地
        File convertedFile = new File(filePath+img.getOriginalFilename());
        try {
            byte[] imgBytes = img.getBytes();
            FileOutputStream fos = new FileOutputStream(convertedFile);
            fos.write(imgBytes);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //写入cos
        String imgUrl = cosClientUtil.sendFile(convertedFile, CosFileMkdir.HeadImg);
        // 删除本地文件
        convertedFile.delete();
        // 写入数据库
        updateUserInformation(User.builder().userId(Holder.getUser()).headUrl(imgUrl).build());
        return Result.success(imgUrl,"修改成功");
    }




}
