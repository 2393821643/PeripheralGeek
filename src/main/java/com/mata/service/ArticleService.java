package com.mata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.EsDoc.ArticleDoc;
import com.mata.dto.ArticleDto;
import com.mata.dto.PageResult;
import com.mata.dto.Result;
import com.mata.pojo.Article;

public interface ArticleService extends IService<Article> {
    /**
     * 添加文章
     */
    Result addArticle(ArticleDto articleDto);

    /**
     * 添加到Mysql
     */
    void addArticleToMysql(Article article);

    /**
     * 添加到Es
     */
    void addArticleToEs(Article article);

    /**
     * 根据用户id 获取文章列表
     */
    Result<PageResult<Article>> getArticleByUserId(Integer userId,Integer page);

    /**
     * 根据文章id获取文章
     */
    Result<Article> getArticleById(Long articlesId);
}
