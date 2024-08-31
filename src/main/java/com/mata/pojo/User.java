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
    @Length(min = 1,max = 30,message = "用户名不能为空或者大于30")
    private String username;

    @TableField("password")
    private String password; // 用户密码

    @TableField("email")
    private String email; // 邮箱

    @TableField("address")
    @Length(min = 0,max = 50,message = "收货地址不能大于50个字")
    private String address; // 收货地址

    @TableField("recipient")
    @Length(min = 0,max = 10,message = "收货人不能大于10个字")
    private String recipient; // 收件人

    @Length(min = 0,max = 1,message = "请输入正确的性别")
    @TableField("sex")
    private String sex; // 性别

    @TableField("phone")
    @Length(min = 11,max = 11,message = "请输入正确的手机号")
    private String phone; // 电话

    @TableField("head_url")
    private String headUrl; // 头像地址

    @TableField("sign")
    @Length(min = 0,max = 30,message = "签名长度不能超过30")
    private String sign; // 用户个性签名
}
