package com.mata.model.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mata.model.auth.dto.RegisterAdminDto;
import com.mata.model.user.dao.UserDao;
import com.mata.common.result.Result;
import com.mata.common.exception.BusinessException;
import com.mata.pojo.User;
import com.mata.model.auth.service.AuthService;
import com.mata.utils.EmailMessage;
import com.mata.common.redisKey.RedisCommonKey;
import com.mata.utils.SendEmailUtil;
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

    @Autowired
    private UserDao userDao;




    /**
     * 发送登录验证码 异步 发送到消息队列
     *
     * @param email 发送验证码到此邮箱
     */
    @Override
    public Result sendLoginCodeMessage(String email) {
        // 发送消息至消息队列
        rabbitTemplate.convertAndSend("sendCodeExchange", "sendLoginCodeKey", email);
        // 返回成功信息
        return Result.success("发送成功");
    }

    /**
     * 发送登录验证码
     *
     * @param email 发送验证码到此邮箱
     */
    @Override
    public void sendLoginCode(String email) {
        // 生成随机6位验证码
        String code = RandomUtil.randomNumbers(6);
        // 保存至Redis
        stringRedisTemplate.opsForValue().set(RedisCommonKey.LOGIN_CODE_PER_KEY + email, code, RedisCommonKey.LOGIN_CODE_TIME, TimeUnit.MINUTES);
        // 向此邮箱发送验证码
        sendEmailUtil.sendEmail(email, EmailMessage.TITLE, EmailMessage.SEND_LOGIN_CODE_MESSAGE_FOREBODY + code + EmailMessage.SEND_LOGIN_CODE_MESSAGE_BEHINDBODY);
    }

    /**
     * 发送修改密码验证码 异步 发送到消息队列
     *
     * @param email 发送验证码到此邮箱
     */
    @Override
    public Result sendChangePasswordCodeMessage(String email) {
        // 发送消息至消息队列
        rabbitTemplate.convertAndSend("sendCodeExchange", "sendChangePasswordCode", email);
        // 返回成功信息
        return Result.success("发送成功");
    }

    /**
     * 发送修改密码验证码
     *
     * @param email 发送验证码到此邮箱
     */
    @Override
    public void sendChangePasswordCode(String email) {
        // 生成随机6位验证码
        String code = RandomUtil.randomNumbers(6);
        // 保存至Redis
        stringRedisTemplate.opsForValue().set(RedisCommonKey.CHANGE_PASSWORD_CODE_PER_KEY + email, code, RedisCommonKey.CHANGE_PASSWORD_CODE_TIME, TimeUnit.MINUTES);
        // 向此邮箱发送验证码
        sendEmailUtil.sendEmail(email, EmailMessage.TITLE, EmailMessage.SEND_CHANGE_PASSWORD_CODE_MESSAGE_FOREBODY + code + EmailMessage.SEND_CHANGE_PASSWORD_CODE_MESSAGE_BEHINDBODY);
    }

    /**
     * 通过验证码登录/注册
     *
     * @param email 邮箱
     * @param code  验证码
     * @return token字符串
     */
    @Override
    public Result<String> loginByOpt(String email, String code) {
        // 查看验证码和邮箱是否对应
        String returnCode = stringRedisTemplate.opsForValue().get(RedisCommonKey.LOGIN_CODE_PER_KEY + email);
        if (!code.equals(returnCode)) {
            return Result.error("验证码和邮箱不对应");
        }
        // 查找此邮箱是否存在用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getUserId)
                .eq(User::getEmail, email);
        User user = userDao.selectOne(wrapper);
        // 不存在创建一个
        if (user == null) {
            user = User.builder()
                    .username("用户" + RandomUtil.randomString(6))
                    .password(RandomUtil.randomString(12))
                    .sex("男")
                    .email(email)
                    .roleId(1)
                    .build();
            userDao.insert(user);
        }
        // 返回token
        StpUtil.login(user.getUserId());
        return Result.success(StpUtil.getTokenValue(), "登录成功");
    }

    /**
     * 修改密码 异步 发送到消息队列
     *
     * @param email    邮箱
     * @param code     验证码
     * @param password 修改的密码
     */
    @Override
    public Result changePasswordMessage(String email, String code, String password) {
        // 判断邮箱和验证码是否对应
        String resultCode = stringRedisTemplate.opsForValue().get(RedisCommonKey.CHANGE_PASSWORD_CODE_PER_KEY + email);
        if (!code.equals(resultCode)) {
            return Result.error("邮箱和验证码不对应");
        }
        // 查找此邮箱用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getUserId)
                .eq(User::getEmail, email);
        User user = userDao.selectOne(wrapper);
        if (user == null) {
            return Result.error("此邮箱不存在用户，请注册");
        }
        // 设置密码 加密
        user.setPassword(SmUtil.sm3(password));
        String userJson = JSONUtil.toJsonStr(user);
        userDao.updateById(user);
        return Result.success("修改成功");
    }

    /**
     * 通过账号/邮箱 密码登录
     * @param account  用户id/邮箱
     * @param password 密码
     * @return token字符串
     */
    @Override
    public Result<String> loginByPassword(String account, String password) {
        //判断accounts是id还是email
        boolean email = Validator.isEmail(account);
        //如果是邮箱
        if (email) {
            User resultUser = getUserByEmailAndPassword(account, password);
            if (resultUser != null) {
                // 登录
                StpUtil.login(resultUser.getUserId());
                return Result.success(StpUtil.getTokenValue(), "登录成功");
            } else {
                return Result.error("密码错误");
            }
        } else {
            // 如果是id
            User resultUser = getUserByIdAndPassword(account, password);
            if (resultUser != null) {
                // 登录
                StpUtil.login(resultUser.getUserId());
                if (resultUser.getRoleId() != 1){
                    return Result.error("此账号角色不在此页面登录");
                }
                return Result.success(StpUtil.getTokenValue(), "登录成功");
            } else {
                return Result.error("密码错误");
            }
        }
    }


    /**
     * 通过email和password查用户
     *
     * @param email    邮箱
     * @param password 密码
     * @return user
     */
    private User getUserByEmailAndPassword(String email, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        //设置条件，password进行加密
        wrapper.select(User::getUserId,User::getRoleId);
        wrapper.eq(User::getEmail, email).eq(User::getPassword, SmUtil.sm3(password));
        return userDao.selectOne(wrapper);
    }

    /**
     * 通过id和password查用户
     *
     * @param id       用户Id
     * @param password 密码
     * @return user
     */
    private User getUserByIdAndPassword(String id, String password) {
        int userId = 0;
        try {
            userId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new BusinessException("密码错误");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        //设置条件，password进行加密
        wrapper.eq(User::getUserId, userId).eq(User::getPassword, SmUtil.sm3(password));
        wrapper.select(User::getUserId,User::getRoleId);
        return userDao.selectOne(wrapper);
    }

    /**
     * 管理员登录
     *
     * @param id       管理员id
     * @param password 管理员密码
     */
    @Override
    public Result<String> adminLogin(String id, String password) {
        // string -> int
        int adminId = 0;
        try {
            adminId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new BusinessException("密码错误");
        }
        // 设置查找条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getUserId,User::getRoleId)
                .eq(User::getUserId, adminId)
                .eq(User::getPassword, SmUtil.sm3(password));
        User resultAdmin = userDao.selectOne(wrapper);
        if (resultAdmin == null) {
            return Result.error("密码错误");
        }
        if (resultAdmin.getRoleId() == 1){
            return Result.error("此账号角色不在此页面登录");
        }
        StpUtil.login(resultAdmin.getUserId());
        return Result.success(StpUtil.getTokenValue(),"登录成功");
    }

    /**
     * 注册管理员
     */
    @Override
    public Result<User> registerAdmin(RegisterAdminDto registerAdminDto) {
        User user = User.builder()
                .username(registerAdminDto.getUsername())
                .password(SmUtil.sm3(registerAdminDto.getPassword()))
                .roleId(3)
                .build();
        userDao.insert(user);
        user.setPassword("");
        return Result.success(user,"注册成功,管理员账号为:"+user.getUserId());
    }
}
