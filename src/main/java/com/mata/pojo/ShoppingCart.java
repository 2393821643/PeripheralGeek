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
@TableName("tb_shopping_cart")
public class ShoppingCart {
    // 购物车id
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    // 用户id
    @TableField("user_id")
    private Integer userId;

    // 商品id
    @TableField("goods_id")
    private Long goodsId;

    // 将要买的数量
    @TableField("goods_will_buy_count")
    private Integer goodsWillBuyCount;
}
