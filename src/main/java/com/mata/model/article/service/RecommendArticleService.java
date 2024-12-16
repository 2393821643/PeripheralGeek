package com.mata.model.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.common.result.Result;
import com.mata.model.article.vo.RecommendArticleVo;
import com.mata.pojo.RecommendArticle;

import java.util.List;

public interface RecommendArticleService extends IService<RecommendArticle> {

    /**
     * 新增推荐文章
     */
    Result addRecommendArticle(Long articleId);

    /**
     * 获取推荐文章
     */
    Result<List<RecommendArticleVo>> getRecommendArticle();

    /**
     * 删除推荐文章
     */
    Result deleteRecommendArticle(Long articleId);
}
