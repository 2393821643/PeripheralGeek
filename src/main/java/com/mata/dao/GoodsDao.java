package com.mata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mata.pojo.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsDao extends BaseMapper<Goods> {
}
