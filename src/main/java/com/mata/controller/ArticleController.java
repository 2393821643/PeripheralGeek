package com.mata.controller;

import com.mata.EsDoc.ArticleDoc;
import com.mata.dto.ArticleDto;
import com.mata.dto.PageResult;
import com.mata.dto.Result;
import com.mata.pojo.Article;
import com.mata.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    /**
     * 添加文章
     */
    @PostMapping("/user")
    public Result addArticle(ArticleDto articleDto){
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
}
