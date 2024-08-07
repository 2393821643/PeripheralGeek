package com.mata.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mata.dao.AdminDao;
import com.mata.enumPackage.Role;
import com.mata.pojo.Admin;
import com.mata.utils.JwtUtil;
import com.mata.dao.UserDao;
import com.mata.dto.Result;
import com.mata.exception.BusinessException;
import com.mata.pojo.User;
import com.mata.service.AuthService;
import com.mata.utils.EmailMessage;
import com.mata.utils.RedisCommonKey;
import com.mata.utils.SendEmailUtil;
import org.redisson.api.RBloomFilter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Qualifier("userBloom")
    private RBloomFilter<Integer> userBloom;



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
                    .build();
            userDao.insert(user);
            // 创建完加入用户id布隆过滤器
            userBloom.add(user.getUserId());
        }
        // 将userId存入token 并返回
        String token = jwtUtil.createToken(user.getUserId(), Role.User);
        return Result.success(token, "登录成功");
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
        rabbitTemplate.convertAndSend("UserExchange", "changeUserKey", userJson);
        return Result.success("修改成功");
    }

    /**
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
                //设置一个token
                String token = jwtUtil.createToken(resultUser.getUserId(), Role.User);
                return Result.success(token, "登录成功");
            } else {
                return Result.error("密码错误");
            }
        } else {
            // 如果是id
            User resultUser = getUserByIdAndPassword(account, password);
            if (resultUser != null) {
                String token = jwtUtil.createToken(resultUser.getUserId(), Role.User);
                return Result.success(token, "登录成功");
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
        wrapper.select(User::getUserId);
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
        wrapper.select(User::getUserId);
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
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Admin::getAdminId)
                .eq(Admin::getAdminId, adminId)
                .eq(Admin::getPassword, SmUtil.sm3(password));
        Admin resultAdmin = adminDao.selectOne(wrapper);
        if (resultAdmin == null) {
            return Result.error("密码错误");
        }
        String token = jwtUtil.createToken(resultAdmin.getAdminId(), Role.Admin);
        return Result.success(token,"登录成功");
    }

}
