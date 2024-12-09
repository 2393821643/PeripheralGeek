package com.mata.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthDao {
    /**
     * 返回当前账号的角色
     */
    String getRoleName(@Param("userId") Integer userId);
}
