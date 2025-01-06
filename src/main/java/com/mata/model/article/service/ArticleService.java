package com.mata.model.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.common.result.Suggest;
import com.mata.model.article.dto.ArticleAuditDto;
import com.mata.model.article.dto.ArticleSearchDto;
import com.mata.model.article.esDoc.ArticleDoc;
import com.mata.model.article.dto.ArticleDto;
import com.mata.model.article.dto.ArticleUpdateDto;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
import com.mata.model.article.vo.ArticleAuditVo;
import com.mata.model.article.vo.ArticleVo;
import com.mata.pojo.Article;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService extends IService<Article> {
    /**
     * 添加文章
     */
    Result addArticle(ArticleDto articleDto);

    /**
     * 根据用户id 获取文章列表
     */
    Result<PageResult<Article>> getArticleByUserId(ArticleSearchDto articleSearchDto);

    /**
     * 根据文章id获取文章
     */
    Result<ArticleVo> getArticleById(Long articlesId);


    /**
     * 根据文章名获取文章
     */
    Result<PageResult<ArticleVo>> getArticleByName(String articleName,Integer page);


    /**
     *  删除文章
     */
    Result deleteArticleById(Long articleId);

    /**
     * 修改文章标题，内容 通过文章Id
     */
    Result updateArticle(ArticleUpdateDto articleUpdateDto);

    /**
     * 修改文章图片 通过文章Id
     */
    Result<String> updateArticleImg(Long articleId, MultipartFile img);

    /**
     * 获取文章推荐词
     */
    Result<List<Suggest>> getSuggest(String articleName);

    /**
     * 获取文章列表 根据审核状态
     */
    Result<PageResult<ArticleAuditVo>> getArticleByState(ArticleSearchDto articleSearchDto);

    /**
     * 管理员审核文章
     */
    Result auditArticle(ArticleAuditDto articleAuditDto);
}
