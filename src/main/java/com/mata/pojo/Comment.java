package com.mata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@TableName("tb_comment")
public class Comment {
    @TableId(value="comment_id", type= IdType.NONE)
    private Long commentId; // 评论id

    @TableField("target_id")
    private Long targetId; // 从属id

    @TableField("comment_context")
    private String commentContext; // 评论内容

    @TableField("user_id")
    private Integer userId; // 用户id

    @TableField("good_count")
    private Integer goodCount; // 点赞数

    @TableField("create_time")
    private LocalDateTime createTime; // 评论时间
}
