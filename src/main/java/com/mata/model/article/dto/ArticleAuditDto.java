package com.mata.model.article.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArticleAuditDto {
    @NotNull(message = "文章id不能为空")
    private Long articleId; // 文章id

    @NotNull(message = "审核代码不能为空")
    private Integer auditCode; // 审核代码 1：通过审核 2：不通过审核

    @Length(max = 30,message = "未通过原因不能为空")
    private String nonPassCause; // 未通过原因不能为空
}
