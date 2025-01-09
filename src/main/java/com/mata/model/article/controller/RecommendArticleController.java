package com.mata.model.article.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.mata.common.result.Result;
import com.mata.model.article.service.RecommendArticleService;
import com.mata.model.article.vo.RecommendArticleVo;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 推荐文章controller
 */
@RestController
@RequestMapping("/recommendArticle")
public class RecommendArticleController {
    @Autowired
    private RecommendArticleService recommendArticleService;

    /**
     * 新增推荐文章
     */
    @SaCheckLogin
    @SaCheckRole(value = {"admin","normal_admin"},mode = SaMode.OR)
    @PostMapping("/{articleId}")
    public Result addRecommendArticle(@PathVariable("articleId") Long articleId) {
        return recommendArticleService.addRecommendArticle(articleId);
    }

    /**
     * 获取推荐文章
     */
    @GetMapping
    public Result<List<RecommendArticleVo>> getRecommendArticle(){
        return recommendArticleService.getRecommendArticle();
    }

    /**
     * 删除推荐文章
     */
    @DeleteMapping("/{articleId}")
    @SaCheckLogin
    @SaCheckRole(value = {"admin","normal_admin"},mode = SaMode.OR)
    public Result deleteRecommendArticle(@PathVariable("articleId") Long articleId){
        return recommendArticleService.deleteRecommendArticle(articleId);
    }

}
