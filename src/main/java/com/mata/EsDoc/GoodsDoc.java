package com.mata.EsDoc;


import com.mata.pojo.Goods;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GoodsDoc {
    private Long goodsId; // 商品id

    private String goodsName; // 商品名

    private String goodsType; // 商品类型

    private Double goodsPrice; // 商品数量

    private String goodsConnectionType; // 商品连接方式

    private String goodsUrl; // 商品图片url

    private String goodsIntroduction; //商品介绍

    private String goodsBrand; // 商品品牌

    private List<String> suggestion; // 商品提示词


    public GoodsDoc(Goods goods) {
        this.goodsId = goods.getGoodsId();
        this.goodsName = goods.getGoodsName();
        this.goodsType = goods.getGoodsType();
        this.goodsPrice = goods.getGoodsPrice();
        this.goodsConnectionType = goods.getGoodsConnectionType();
        this.goodsUrl = goods.getGoodsUrl();
        this.goodsIntroduction = goods.getGoodsIntroduction();
        this.goodsBrand = goods.getGoodsBrand();
        List<String> suggestionList = new ArrayList<>();
        Collections.addAll(suggestionList,this.goodsName,this.goodsType,this.goodsConnectionType,this.goodsBrand);
        this.suggestion = suggestionList;
    }

}
