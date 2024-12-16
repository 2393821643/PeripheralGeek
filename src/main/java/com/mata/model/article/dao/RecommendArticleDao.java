package com.mata.model.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mata.model.article.vo.RecommendArticleVo;
import com.mata.pojo.RecommendArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecommendArticleDao extends BaseMapper<RecommendArticle> {
    List<RecommendArticleVo> getAllRecommendArticle();
}
