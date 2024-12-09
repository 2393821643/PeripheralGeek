package com.mata.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.dao.ReceiptInformationDao;
import com.mata.dao.UserDao;
import com.mata.dto.ReceiptInformationDto;
import com.mata.dto.Result;
import com.mata.enumPackage.CosFileMkdir;
import com.mata.enumPackage.UserPositioning;
import com.mata.pojo.ReceiptInformation;
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
import org.springframework.context.annotation.Bean;
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
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    private RedissonClient redissonClient;
//
//    @Autowired
//    @Qualifier("userBloom")
//    private RBloomFilter<Integer> userBloom;

    @Autowired
    private CosClientUtil cosClientUtil;

    @Autowired
    private ReceiptInformationDao receiptInformationDao;

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
    public Result<User> getUserInformationById(Integer userId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getUserId, User::getEmail, User::getSex, User::getUsername, User::getHeadUrl, User::getSign)
                .eq(User::getUserId, userId);
        User resultUser = getOne(wrapper);
        return Result.success(resultUser);
    }

    /**
     * 修改用户信息
     */
    @Override
    public Result updateUserInformationMessage(User user) {
        // 注入userId
        int userId = StpUtil.getLoginIdAsInt();
        user.setUserId(userId);
        // 修改个人信息
        return Result.success("修改成功");
    }

    /**
     * 通过用户名查找用户
     */
    @Override
    public Result<List<User>> getUserInformationByName(String username) {
        // 查数据库
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getUserId, User::getEmail, User::getSex, User::getUsername, User::getHeadUrl, User::getSign)
                .likeRight(User::getUsername, username);
        List<User> userList = list(wrapper);
        return Result.success(userList);
    }

    /**
     * 修改用户头像
     */
    @Override
    public Result<String> updateUserHeader(MultipartFile img) {
        // 图片写入本地
        File convertedFile = new File(filePath + img.getOriginalFilename());
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
        updateUserInformation(User.builder().userId(StpUtil.getLoginIdAsInt()).headUrl(imgUrl).build());
        return Result.success(imgUrl, "修改成功");
    }

    /**
     * 设置用户默认收获信息
     */
    @Override
    public Result addOrUpdateReceiptInformation(ReceiptInformationDto receiptInformationDto) {
        int userId = StpUtil.getLoginIdAsInt();
        boolean isPhone = Validator.isMobile(receiptInformationDto.getPhone());
        if (!isPhone){
            return Result.error("电话号码错误");
        }
        // 先查是否存在
        ReceiptInformation receiptInformation = receiptInformationDao.selectById(userId);
        // 不空修改
        if (!ObjectUtil.isEmpty(receiptInformation)){
            receiptInformation.setAddress(receiptInformationDto.getAddress());
            receiptInformation.setRecipient(receiptInformationDto.getRecipient());
            receiptInformation.setPhone(receiptInformationDto.getPhone());
            receiptInformationDao.updateById(receiptInformation);
        }else { // 空添加
            ReceiptInformation newReceiptInformation = BeanUtil.copyProperties(receiptInformationDto, ReceiptInformation.class);
            newReceiptInformation.setUserId(userId);
            receiptInformationDao.insert(newReceiptInformation);
        }
        return Result.success(null,"修改成功");
    }

    /**
     * 获取用户默认收获信息
     */
    @Override
    public Result<ReceiptInformation> getReceiptInformation() {
        ReceiptInformation receiptInformation = receiptInformationDao.selectById(StpUtil.getLoginIdAsInt());
        return Result.success(receiptInformation);
    }


}
