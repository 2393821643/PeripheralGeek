package com.mata.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.mata.esDoc.ArticleDoc;
import com.mata.dto.ArticleDto;
import com.mata.dto.ArticleUpdateDto;
import com.mata.dto.PageResult;
import com.mata.dto.Result;
import com.mata.pojo.Article;
import com.mata.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/list/userId")
    public Result<PageResult<Article>> getArticleByUserId(@RequestParam("userId") Integer userId, @RequestParam("page") Integer page){
        return articleService.getArticleByUserId(userId,page);
    }

    /**
     * 根据文章id获取文章
     */
    @GetMapping("/{articlesId}")
    public Result<Article> getArticleById(@PathVariable("articlesId") Long articlesId){
        return articleService.getArticleById(articlesId);
    }

    /**
     * 根据文章名获取文章
     */
    @GetMapping("/list")
    public Result<PageResult<ArticleDoc>> getArticleByName(@RequestParam("articleName") String articleName,@RequestParam("page") Integer page){
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
    public Result updateArticleImg(@RequestParam("articleId")Long articleId, @RequestParam("articleImg")MultipartFile img){
        return articleService.updateArticleImg(articleId,img);
    }



}
