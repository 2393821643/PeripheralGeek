package com.mata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mata.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleDao extends BaseMapper<Article> {
}
