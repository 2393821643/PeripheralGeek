package com.mata.model.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mata.pojo.Audit;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuditDao extends BaseMapper<Audit> {
}
