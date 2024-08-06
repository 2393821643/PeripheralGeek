package com.mata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@TableName("tb_order")
public class Order {
    @TableId(value="out_trade_no", type= IdType.NONE)
    private Long outTradeNo;

    @TableField("user_id")
    private Integer userId;

    @TableField("book_id")
    private Long bookId;

    @TableField("price")
    private Double price;

    @TableField("goods_name")
    private String goodsName;

    @TableField("state")
    private String state;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("address")
    private String address;

    @TableField("recipient")
    private String recipient;

    @TableField("phone")
    private String phone;

    @TableField("bookUrl")
    private String bookUrl;
}
