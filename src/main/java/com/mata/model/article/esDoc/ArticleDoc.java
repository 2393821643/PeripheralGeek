package com.mata.model.article.esDoc;


import com.mata.pojo.Article;
import lombok.*;

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



    public ArticleDoc(Article article) {
        this.articleId = article.getArticleId();
        this.articleTitle = article.getArticleTitle();
        this.articleContextUrl = article.getArticleContextUrl();
        this.articleImgUrl = article.getArticleImgUrl();
        this.userId = article.getUserId();
        this.briefIntroduction = article.getBriefIntroduction();
        this.articleState = article.getArticleState();
        List<String> suggestionList = new ArrayList<>();
        Collections.addAll(suggestionList,this.articleTitle);
        this.suggestion = suggestionList;
    }
}
