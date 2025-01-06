package com.mata.model.article.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ArticleSearchDto {
    // 文章名
    private String title;

    // 文章id
    private Long articleId;

    // 作者id
    private Integer userId;

    // 状态 已审核 未审核 审核未通过
    private String state;

    // 页
    private Integer page;
}
