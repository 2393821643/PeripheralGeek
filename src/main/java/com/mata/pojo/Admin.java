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
    @TableId(value="book_id", type= IdType.NONE)
    private Integer adminId;

    @TableField("book_name")
    private String adminName;
}
