package com.mata.listener;

import cn.hutool.json.JSONUtil;
import com.mata.pojo.Order;
import com.mata.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderMessageListener {
    @Autowired
    private OrderService orderService;

    /**
     * 监听订单信息 写入mysql
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "orderCreateQueue"),
            exchange = @Exchange(name = "OrderExchange",type = ExchangeTypes.DIRECT),
            key = {"createOrderKey"}
    ))
    public void listenCreateOrderToMysql(String orderJson){
        Order order = JSONUtil.toBean(orderJson, Order.class);
        orderService.createOrderToMysql(order);
    }

    /**
     * 监听写入数据库 商品数量
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "updateCountQueue"),
            exchange = @Exchange(name = "UpdateCountExchange",delayed = "true"),
            key = {"updateCountKey"}
    ))
    public void listenUpdateCount(String goodsIdStr){
        orderService.decreaseGoodsCount(Long.valueOf(goodsIdStr));
    }

    /**
     * 监听修改数据库 修改订单信息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "updateOrderQueue"),
            exchange = @Exchange(name = "OrderExchange",type = ExchangeTypes.DIRECT),
            key = {"updateOrderKey"}
    ))
    public void listenUpdateOrder(String orderJson){
        orderService.updateOrder(JSONUtil.toBean(orderJson,Order.class));
    }


}
