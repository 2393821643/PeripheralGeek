package com.mata.model.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.common.result.PageResult;
import com.mata.model.user.dto.AdminUpdateDto;
import com.mata.model.user.dto.UserConditionDto;
import com.mata.model.user.dto.ReceiptInformationDto;
import com.mata.common.result.Result;
import com.mata.model.user.dto.UserUpdateDto;
import com.mata.pojo.ReceiptInformation;
import com.mata.pojo.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends IService<User> {
    /**
     * 修改用户信息 接收消息队列
     */
    void updateUserInformation(User user);

    /**
     * 获取某个用户的个人信息 通过userid
     */
    Result<User> getUserInformationById(Integer userId);

    /**
     * 修改用户信息 发送消息队列
     */
    Result updateUserInformationMessage(UserUpdateDto user);


    /**
     * 通过用户名查用户信息
     */
    Result<PageResult<User>> getUserInformationByName(UserConditionDto userConditionDto);

    /**
     * 修改用户头像
     */
    Result<String> updateUserHeader(MultipartFile img);


    /**
     * 设置用户默认收获信息
     */
    Result addOrUpdateReceiptInformation(ReceiptInformationDto receiptInformationDto);

    /**
     * 获取用户默认收获信息
     */
    Result<ReceiptInformation> getReceiptInformation();

    /**
     * 查看管理员列表
     */
    Result<PageResult<User>> adminList(UserConditionDto userConditionDto);

    /**
     * 修改管理员信息
     */
    Result updateAdmin(AdminUpdateDto adminUpdateDto);

    /**
     * 删除管理员账号
     */
    Result deleteAdmin(Integer userId);
}
