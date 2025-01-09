package com.mata.model.article.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
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
import com.mata.model.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    /**
     * 添加文章
     */
    @PostMapping("/user")
    @SaCheckLogin
    @SaCheckRole("user")
    public Result addArticle(@Validated ArticleDto articleDto){
       return articleService.addArticle(articleDto);
    }

    /**
     * 根据用户id 获取文章列表
     */
    @PostMapping("/list/userId")
    public Result<PageResult<Article>> getArticleByUserId(@RequestBody ArticleSearchDto articleSearchDto){
        return articleService.getArticleByUserId(articleSearchDto);
    }

    /**
     * 根据文章id获取文章
     */
    @GetMapping("/{articlesId}")
    public Result<ArticleVo> getArticleById(@PathVariable("articlesId") Long articlesId){
        return articleService.getArticleById(articlesId);
    }

    /**
     * 根据文章名获取文章
     */
    @GetMapping("/list")
    public Result<PageResult<ArticleVo>> getArticleByName(@RequestParam("articleName") String articleName,@RequestParam("page") Integer page){
        return articleService.getArticleByName(articleName,page);
    }

    /**
     *  删除文章
     */
    @DeleteMapping("/user/{articleId}")
    @SaCheckLogin
    @SaCheckRole("user")
    public Result deleteArticleById(@PathVariable("articleId") Long articleId){
        return articleService.deleteArticleById(articleId);
    }

    /**
     * 修改文章标题，内容 通过文章Id
     */
    @PutMapping("/user")
    @SaCheckLogin
    public Result updateArticle(@Validated ArticleUpdateDto articleUpdateDto){
        return articleService.updateArticle(articleUpdateDto);
    }

    /**
     * 修改文章图片 通过文章Id
     */
    @PutMapping("/user/img")
    @SaCheckLogin
    public Result<String> updateArticleImg(@RequestParam("articleId")Long articleId, @RequestParam("articleImg")MultipartFile img){
        return articleService.updateArticleImg(articleId,img);
    }

    /**
     * 获取文章推荐词
     */
    @GetMapping("/suggest/{articleName}")
    public Result<List<Suggest>> getSuggest(@PathVariable("articleName") String articleName){
        return articleService.getSuggest(articleName);
    }

    /**
     * 获取文章列表 根据审核状态
     */
    @PostMapping("/list/state")
    @SaCheckLogin
    public Result<PageResult<ArticleAuditVo>> getArticleByState(@RequestBody ArticleSearchDto articleSearchDto){
        return articleService.getArticleByState(articleSearchDto);
    }

    /**
     * 管理员审核文章
     */
    @PutMapping("/audit")
    @SaCheckLogin
    @SaCheckRole(value = {"admin","normal_admin"},mode = SaMode.OR)
    public Result auditArticle(@RequestBody @Validated ArticleAuditDto articleAuditDto){
        return articleService.auditArticle(articleAuditDto);
    }



}
