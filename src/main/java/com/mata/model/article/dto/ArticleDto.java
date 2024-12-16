package com.mata.model.article.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ArticleDto {
    @NotEmpty(message = "标题长度要大于0，小于50")
    @Length(min = 1,max = 50,message = "标题长度要大于0，小于50")
    private String title; // 文章标题

    @NotNull(message = "文章内容不能为空")
    @NotEmpty(message = "文章内容不能为空")
    private String context; // 文章内容

    @NotNull(message = "商品图片不能为空")
    private MultipartFile articleImg; // 文章封面

}
