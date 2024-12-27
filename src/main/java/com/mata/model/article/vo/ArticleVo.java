package com.mata.model.article.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ArticleVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long articleId; // 文章id

    private String articleTitle; // 文章标题

    private String articleContextUrl; // 文章内容url

    private String articleImgUrl; // 文章封面url

    private String articleState; // 文章审核状态

    private Integer userId; // 文章作者id

    private String username; // 作者用户名

    private String briefIntroduction; // 文章简介

    private String headUrl; // 作者头像url

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime createTime; // 创建时间
}
