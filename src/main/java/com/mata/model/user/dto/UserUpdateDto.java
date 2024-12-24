package com.mata.model.user.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserUpdateDto {
    @Length(min = 1,max = 30,message = "用户名不能为空或者大于30")
    private String username;

    @Length(min = 0,max = 1,message = "请输入正确的性别")
    private String sex; // 性别


    @Length(min = 0,max = 30,message = "签名长度不能超过30")
    private String sign; // 用户个性签名
}
