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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("tb_admin")
public class Admin {
    @TableId(value="admin_id", type= IdType.AUTO)
    private Integer adminId;

    @Length(min = 1,max = 30,message = "用户名长度要大于0，小于30")
    @TableField("admin_Name")
    private String adminName;

    @TableField("password")
    private String password;
}
