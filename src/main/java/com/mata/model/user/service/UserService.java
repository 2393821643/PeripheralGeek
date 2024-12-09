package com.mata.model.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.model.user.dto.ReceiptInformationDto;
import com.mata.common.result.Result;
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
    Result updateUserInformationMessage(User user);


    /**
     * 通过用户名查用户信息
     */
    Result<List<User>> getUserInformationByName(String username);

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
}
