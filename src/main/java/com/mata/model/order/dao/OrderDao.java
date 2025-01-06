package com.mata.model.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mata.model.order.vo.OrderSendMailVo;
import com.mata.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDao extends BaseMapper<Order> {
    /**
     * 根据订单号查找此订单的订单号，邮箱，商品名
     */
    OrderSendMailVo getOrderToSendEmail(@Param("outTradeNo") Long outTradeNo);
}
