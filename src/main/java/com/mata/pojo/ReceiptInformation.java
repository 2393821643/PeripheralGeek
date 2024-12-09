package com.mata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 用户默认收件地址
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("tb_receipt_information")
public class ReceiptInformation {
    // 用户id
    @TableId(value="user_id")
    private Integer userId;

    // 收件电话
    @TableField("phone")
    private String phone;

    // 地址
    @TableField("address")
    private String address;

    // 收件人姓名
    @TableField("recipient")
    private String recipient;
}
