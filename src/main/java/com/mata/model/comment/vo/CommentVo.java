package com.mata.model.comment.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CommentVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long commentId; // 评论id

    private Long targetId; //从属id

    private Long userId; //用户id

    private String username; // 用户名

    private String headUrl; // 用户头像

    private String commentContext; // 评论内容

    private Integer goodCount; // 点赞数

    private Boolean isGood; // 是否被点赞

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime createTime; // 评论时间
}
