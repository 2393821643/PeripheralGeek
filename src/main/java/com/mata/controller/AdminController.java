package com.mata.controller;

import com.mata.dto.Result;
import com.mata.pojo.Admin;
import com.mata.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    /**
     * 查找管理员信息
     */
    @GetMapping("/information")
    public Result<Admin> getAdminInformation(){
        return adminService.getAdminInformation();
    }

    /**
     * 修改管理员信息
     */
    @PutMapping("/information")
    public Result updateAdminInformation(@RequestBody @Validated Admin admin){
        return adminService.updateAdminInformation(admin);
    }
}
