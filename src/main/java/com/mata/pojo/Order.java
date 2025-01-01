package com.mata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@TableName("tb_order")
public class Order {
    // 订单号
    @TableId(value="out_trade_no", type= IdType.NONE)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long outTradeNo;

    // 用户id
    @TableField("user_id")
    private Integer userId;

    // 商品id
    @TableField("goods_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long goodsId;

    // 价格
    @TableField("price")
    private Double price;

    // 商品名
    @TableField("goods_name")
    private String goodsName;

    // 状态
    @TableField("state")
    private String state;

    // 订单时间
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    // 地址
    @TableField("address")
    private String address;

    // 收件人
    @TableField("recipient")
    private String recipient;

    // 收件手机号
    @TableField("phone")
    private String phone;

    // 商品图片
    @TableField("goods_url")
    private String goodsUrl;

    // 商品数量
    @TableField("goods_count")
    private Integer goodsCount;
}
