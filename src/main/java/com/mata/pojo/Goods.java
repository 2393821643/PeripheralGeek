package com.mata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@TableName("tb_goods")
public class Goods {
    @TableId(value ="goods_id" ,type = IdType.NONE)
    private Long goodsId; // 商品id

    @TableField("goods_name")
    private String goodsName; // 商品名

    @TableField("goods_type")
    private String goodsType; // 商品类型

    @TableField("goods_count")
    private Integer goodsCount;  // 商品数量

    @TableField("goods_price")
    private Double goodsPrice; // 商品数量

    @TableField("goods_connect_type")
    private String goodsConnectionType; // 商品连接方式

    @TableField("goods_url")
    private String goodsUrl; // 商品图片url

    @TableField("goods_introduction")
    private String goodsIntroduction; //商品介绍

    @TableField("goods_brand")
    private String goodsBrand; // 商品品牌
}
