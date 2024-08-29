package com.mata.listener;

import cn.hutool.json.JSONUtil;
import com.mata.enumPackage.CosFileMkdir;
import com.mata.pojo.Goods;
import com.mata.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GoodsMessageListener {
    @Autowired
    private GoodsService goodsService;

    /**
     * 添加商品信息 到es和mysql
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "addGoodsQueue"),
            exchange = @Exchange(name = "GoodsExchange",type = ExchangeTypes.DIRECT),
            key = {"addGoodsKey"}
    ))
    public void addGoods(String goodsJson){
        Goods goods = JSONUtil.toBean(goodsJson, Goods.class);
        goodsService.addGoodsToMysql(goods);
        goodsService.addGoodsToEs(goods);
    }

    /**
     * 删除商品信息 到es和mysql
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "deleteGoodsQueue"),
            exchange = @Exchange(name = "GoodsExchange",type = ExchangeTypes.DIRECT),
            key = {"deleteGoodsKey"}
    ))
    public void deleteGoods(String goodsIdStr){
        Long goodId = Long.valueOf(goodsIdStr);
        goodsService.deleteGoodsToMysql(goodId);
        goodsService.deleteGoodsToEs(goodsIdStr);
    }

    /**
     * 修改商品信息 到es和mysql
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "updateGoodsQueue"),
            exchange = @Exchange(name = "GoodsExchange",type = ExchangeTypes.DIRECT),
            key = {"updateGoodsKey"}
    ))
    public void updateGoods(String goodsJson){
        Goods goods = JSONUtil.toBean(goodsJson, Goods.class);
        goodsService.updateGoodsToMysql(goods);
        goodsService.updateGoodsToEs(goods);
    }

    /**
     * 修改商品图片 到es和mysql
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "updateGoodsImgQueue"),
            exchange = @Exchange(name = "GoodsExchange",type = ExchangeTypes.DIRECT),
            key = {"updateGoodsImgKey"}
    ))
    public void updateGoodsImg(String goodsJson){
        Goods goods = JSONUtil.toBean(goodsJson, Goods.class);
        goodsService.updateGoodsToMysql(goods);
        // 判断图片字段或介绍html是否为空，来选择更新哪个
        if (goods.getGoodsUrl() != null){
            goodsService.updateGoodsFileToEs(goods, CosFileMkdir.GoodsImg);
        } else {
            goodsService.updateGoodsFileToEs(goods, CosFileMkdir.GoodsHtmlImg);
        }
    }
}
