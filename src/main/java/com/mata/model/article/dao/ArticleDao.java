package com.mata.model.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mata.model.article.dto.ArticleSearchDto;
import com.mata.model.article.vo.ArticleAuditVo;
import com.mata.model.article.vo.ArticleVo;
import com.mata.model.comment.vo.CommentVo;
import com.mata.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleDao extends BaseMapper<Article> {
    /**
     * 根据文章id获取文章
     */
    ArticleVo getArticleById(@Param("articleId") Long articleId);

    /**
     * 获取文章审核列表
     */
    IPage<ArticleAuditVo> getArticleAuditList(@Param("page") IPage<ArticleAuditVo> page,@Param("articleSearchDto") ArticleSearchDto articleSearchDto);

}
