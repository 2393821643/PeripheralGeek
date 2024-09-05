package com.mata.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ArticleDto {
    private String title; // 文章标题

    private String context; // 文章内容

    private MultipartFile articleImg; // 文章封面
}
