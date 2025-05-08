package com.mata.model.article.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ArticleUpdateDto {
    @NotNull(message = "文章id不能为空")
    private Long articleId; //文章id

    @NotNull(message = "文章内容不能为空")
    @NotEmpty(message = "文章内容不能为空")
    private String context; // 文章内容

    @NotEmpty(message = "标题长度要大于8，小于50")
    @Length(min = 8,max = 50,message = "标题长度要大于8，小于50")
    private String articleTitle; // 文章标题

    private String articleState; // 文章审核状态
}
