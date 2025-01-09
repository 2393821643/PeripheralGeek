package com.mata.model.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.common.result.PageResult;
import com.mata.model.user.dto.AdminUpdateDto;
import com.mata.model.user.dto.UserConditionDto;
import com.mata.model.user.dao.ReceiptInformationDao;
import com.mata.model.user.dao.UserDao;
import com.mata.model.user.dto.ReceiptInformationDto;
import com.mata.common.result.Result;
import com.mata.common.enumPackage.CosFileMkdir;
import com.mata.model.user.dto.UserUpdateDto;
import com.mata.pojo.ReceiptInformation;
import com.mata.pojo.User;
import com.mata.model.user.service.UserService;
import com.mata.utils.CosClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
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
        wrapper.select(User::getUserId, User::getEmail, User::getSex, User::getUsername, User::getHeadUrl, User::getSign,User::getRoleId)
                .eq(User::getUserId, userId);
        User resultUser = getOne(wrapper);
        return Result.success(resultUser);
    }

    /**
     * 修改用户信息
     */
    @Override
    public Result updateUserInformationMessage(UserUpdateDto userUpdateDto) {
        User user = BeanUtil.copyProperties(userUpdateDto, User.class);
        // 注入userId
        int userId = StpUtil.getLoginIdAsInt();
        user.setUserId(userId);
        // 修改个人信息
        updateById(user);
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

    /**
     * 查看管理员列表
     */
    @Override
    public Result<PageResult<User>> adminList(UserConditionDto userConditionDto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getUserId,User::getUsername)
                        .in(User::getRoleId,2,3);
        wrapper.select(User::getUserId,User::getUsername);
        if (userConditionDto.getUserId() != null){
            wrapper.eq(User::getUserId,userConditionDto.getUserId());
        }
        if (!StrUtil.isEmpty(userConditionDto.getUsername())){
            wrapper.eq(User::getUsername,userConditionDto.getUsername());
        }

        Page<User> page = new Page<>(userConditionDto.getPageNum(),20);
        Page<User> userPage = this.page(page, wrapper);
        PageResult<User> pageResult = new PageResult<>(userPage.getTotal(),userPage.getRecords());
        return Result.success(pageResult);
    }

    /**
     * 修改管理员信息
     */
    @Override
    public Result updateAdmin(AdminUpdateDto adminUpdateDto) {
        // 查找此用户
        User user = getById(adminUpdateDto.getUserId());
        if (user == null){
            return Result.error("此账号不存在");
        }
        // 检查角色 如果此账号是普通管理员 无法修改超级管理员
        if (user.getRoleId() == 2 && Objects.equals(StpUtil.getRoleList().get(0), "normal_admin")){
            return Result.error("你不能修改超级管理员的信息");
        }
        // 校验参数
        if (!StrUtil.isEmpty(adminUpdateDto.getUsername())){
            if (adminUpdateDto.getUsername().length()>30){
                return Result.error("用户名不能大于30");
            }
            user.setUsername(adminUpdateDto.getUsername());
        }
        if (!StrUtil.isEmpty(adminUpdateDto.getPassword())){
            if (adminUpdateDto.getPassword().length()>30 || adminUpdateDto.getPassword().length()<5 ){
                return Result.error("密码长度大于5，小于30");
            }
            user.setPassword(SmUtil.sm3(adminUpdateDto.getPassword()));
        }
        this.updateById(user);
        return Result.success("修改成功");
    }

    /**
     * 删除管理员账号
     */
    @Override
    public Result deleteAdmin(Integer userId) {
        // 查找此用户
        User user = getById(userId);
        if (user == null){
            return Result.error("此账号不存在");
        }
        // 检查是不是超级管理员
        if (user.getRoleId() == 2){
            return Result.error("超级管理员无法被删除");
        }
        // 检查是否是当前账号
        if (user.getUserId() == StpUtil.getLoginIdAsInt()){
            return Result.error("无法删除自己的账号");
        }
        // 删除账号
        removeById(userId);
        // 下线账号
        StpUtil.kickout(userId);
        return Result.success("删除成功");
    }
}
