package com.mata.dao;

import com.mata.EsDoc.ArticleDoc;
import com.mata.dto.PageResult;
import com.mata.dto.Result;

public interface ArticleDocDao {
    /**
     * 添加文章
     */
    void addArticle(ArticleDoc articleDoc);


    /**
     * 根据文章名获取文章
     */
    PageResult<ArticleDoc> getArticleByName(String articleName,Integer page);

    /**
     *  删除文章
     */
    void deleteArticle(String articleId);

    /**
     * 修改文章信息
     */
    void updateArticle(ArticleDoc articleDoc);
}
