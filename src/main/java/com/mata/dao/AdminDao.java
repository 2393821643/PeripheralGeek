package com.mata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mata.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.Max;

@Mapper
public interface AdminDao extends BaseMapper<Admin> {
}
