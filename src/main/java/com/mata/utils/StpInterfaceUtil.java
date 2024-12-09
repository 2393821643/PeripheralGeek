package com.mata.utils;

import cn.dev33.satoken.stp.StpInterface;
import com.mata.dao.AuthDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class StpInterfaceUtil implements StpInterface {
    @Autowired
    private AuthDao authDao;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> list = new ArrayList<String>();
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> list = new ArrayList<String>();
        String roleName = authDao.getRoleName(Integer.valueOf(loginId.toString()));
        Collections.addAll(list,roleName);
        return list;
    }
}
