package com.mata.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateDto {
    @NotNull(message = "账号不能为空")
    private Integer userId;

    private String username;

    private String password;
}
