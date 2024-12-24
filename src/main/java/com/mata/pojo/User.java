package com.mata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("tb_user")
public class User {
    @TableId(value="user_id", type= IdType.AUTO)
    private Integer userId; // 用户id

    @TableField("username")
    private String username;

    @TableField("password")
    private String password; // 用户密码

    @TableField("email")
    private String email; // 邮箱

    @TableField("sex")
    private String sex; // 性别

    @TableField("head_url")
    private String headUrl; // 头像地址

    @TableField("sign")
    private String sign; // 用户个性签名

    @TableField("role_id")
    private Integer roleId;
}
