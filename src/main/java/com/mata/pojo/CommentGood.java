package com.mata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@TableName("tb_comment_good")
public class CommentGood {
    @TableId(value ="id" ,type = IdType.NONE)
    private String id; // 点赞记录id(用户id+评论id)

    @TableField("comment_id")
    private Long commentId; // 评论id

    @TableField("user_id")
    private Long userId; // 用户id

    @TableField("target_id")
    private Long targetId; // 从属id

}
