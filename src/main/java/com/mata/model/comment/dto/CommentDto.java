package com.mata.model.comment.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CommentDto {
    // 从评论id
    @NotNull(message = "评论的对象不能为空")
    private Long targetId;

    // 评论内容
    @NotEmpty(message = "评论内容要大于0，小于255")
    @Length(min = 1,max = 255,message = "商品名长度要大于0，小于255")
    private String commentContext;
}
