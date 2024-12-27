package com.mata.model.article.esDoc;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.mata.pojo.Article;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ArticleDoc {
    private Long articleId; // 文章id

    private String articleTitle; // 文章标题

    private String articleContextUrl; // 文章内容url

    private String articleImgUrl; // 文章封面url

    private String articleState; // 文章审核状态

    private Integer userId; // 文章作者id

    private String briefIntroduction; // 文章简介

    private List<String> suggestion; // 商品提示词

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime; // 创建时间



    public ArticleDoc(Article article) {
        this.articleId = article.getArticleId();
        this.articleTitle = article.getArticleTitle();
        this.articleContextUrl = article.getArticleContextUrl();
        this.articleImgUrl = article.getArticleImgUrl();
        this.userId = article.getUserId();
        this.briefIntroduction = article.getBriefIntroduction();
        this.articleState = article.getArticleState();
        this.createTime = article.getCreateTime();
        List<String> suggestionList = new ArrayList<>();
        Collections.addAll(suggestionList,this.articleTitle);
        this.suggestion = suggestionList;
    }
}
