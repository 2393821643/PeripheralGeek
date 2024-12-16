package com.mata.model.article.vo;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RecommendArticleVo implements Serializable {
    private Long articleId; // 文章id
    private String articleTitle; // 文章标题
    private String articleImgUrl; // 文章图片url

}
