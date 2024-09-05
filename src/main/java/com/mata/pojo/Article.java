package com.mata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("tb_article")
public class Article {
    @TableId(value="article_id", type= IdType.NONE)
    private Long articleId; // 文章id

    @TableField("article_title")
    private String articleTitle; // 文章标题

    @TableField("article_context_url")
    private String articleContextUrl; // 文章内容url

    @TableField("article_img_url")
    private String articleImgUrl; // 文章封面url

    @TableField("article_state")
    private String articleState; // 文章审核状态

    @TableField("user_id")
    private Integer userId; // 文章作者id

    @TableField("create_time")
    private LocalDateTime createTime;
}
