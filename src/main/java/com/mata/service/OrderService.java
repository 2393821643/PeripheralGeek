package com.mata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.dto.BuyMessageDto;
import com.mata.dto.Result;
import com.mata.pojo.Order;

import javax.servlet.http.HttpServletRequest;

public interface OrderService extends IService<Order> {
    /**
     *  购买商品，返回支付html
     */
    Result<String> buyGoods(Long goodsId, BuyMessageDto buyMessageDto);

    /**
     * 订单写入数据库
     */
    void createOrderToMysql(Order order);

    /**
     *  改变商品数量 数据库
     *  从缓存读数量 写入数据库
     */
    void decreaseGoodsCount(Long goodsId);

    /**
     * 修改订单
     */
    void updateOrder(Order order);

    /**
     * 支付/退款回调接口
     */
    void payNotice(HttpServletRequest httpServletRequest);
}
