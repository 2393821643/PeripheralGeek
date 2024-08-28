package com.mata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.dto.Result;
import com.mata.pojo.Admin;
import com.mata.pojo.User;

public interface AdminService extends IService<Admin> {
    /**
     * 查找管理员信息
     */
    Result<Admin> getAdminInformation();

    /**
     * 修改管理员信息
     */
    Result updateAdminInformation(Admin admin);
}
