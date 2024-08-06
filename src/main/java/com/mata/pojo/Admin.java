package com.mata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("tb_admin")
public class Admin {
    @TableId(value="admin_id", type= IdType.AUTO)
    private Integer adminId;

    @TableField("admin_Name")
    private String adminName;

    @TableField("password")
    private String password;
}
