package com.mata.model.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mata.model.article.vo.ArticleVo;
import com.mata.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleDao extends BaseMapper<Article> {
    /**
     * 根据文章id获取文章
     */
    ArticleVo getArticleById(@Param("articleId") Long articleId);
}
