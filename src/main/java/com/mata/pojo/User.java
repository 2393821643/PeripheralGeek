package com.mata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

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

    @TableField("address")
    private String address; // 收货地址

    @TableField("recipient")
    private String recipient; // 收件人

    @TableField("sex")
    private String sex; // 性别

    @TableField("phone")
    private String phone; // 电话
}
