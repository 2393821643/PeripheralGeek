package com.mata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.dao.AdminDao;
import com.mata.dto.Result;
import com.mata.holder.Holder;
import com.mata.pojo.Admin;
import com.mata.service.AdminService;
import org.springframework.stereotype.Service;


@Service
public class AdminServiceImpl extends ServiceImpl<AdminDao, Admin> implements AdminService {

    /**
     * 查找管理员信息
     */
    @Override
    public Result<Admin> getAdminInformation() {
        Integer adminId = Holder.getUser();
        Admin admin = getOne(new LambdaQueryWrapper<Admin>().select(Admin::getAdminId,Admin::getAdminName).eq(Admin::getAdminId, adminId));
        return Result.success(admin);
    }

    /**
     * 修改管理员信息
     */
    @Override
    public Result updateAdminInformation(Admin admin) {
        updateById(admin);
        return Result.success("修改成功");
    }
}
