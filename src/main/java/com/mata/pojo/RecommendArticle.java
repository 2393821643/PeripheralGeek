package com.mata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("tb_recommend_article")
public class RecommendArticle {
    @TableId(value="article_id", type= IdType.NONE)
    private Long articleId;
}
