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

    // 作者id
    private Integer userId;

    // 页
    private Integer page;
}
