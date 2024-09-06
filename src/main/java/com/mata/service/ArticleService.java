package com.mata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.EsDoc.ArticleDoc;
import com.mata.dto.ArticleDto;
import com.mata.dto.ArticleUpdateDto;
import com.mata.dto.PageResult;
import com.mata.dto.Result;
import com.mata.pojo.Article;
import org.springframework.web.multipart.MultipartFile;

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


    /**
     * 根据文章名获取文章
     */
    Result<PageResult<ArticleDoc>> getArticleByName(String articleName,Integer page);


    /**
     *  删除文章
     */
    Result deleteArticleById(Long articleId);

    /**
     *  删除文章从mysql
     */
    void deleteToMysql(Long articleId);

    /**
     *  删除文章从es
     */
    void deleteToEs(String articleId);

    /**
     * 修改文章标题，内容 通过文章Id
     */
    Result updateArticle(ArticleUpdateDto articleUpdateDto);

    /**
     * 修改文章信息到Mysql
     */
    void updateToMysql(Article article);

    /**
     * 修改文章信息到es
     */
    void updateToEs(Article article);

    /**
     * 修改文章图片 通过文章Id
     */
    Result updateArticleImg(Long articleId, MultipartFile img);

}
