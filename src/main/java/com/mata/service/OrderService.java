package com.mata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.dto.BuyMessageDto;
import com.mata.dto.PageResult;
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

    /**
     * 继续支付
     */
    Result<String> continuePay(Long outTradeNo);

    /**
     * 查看订单信息 只能看当前账号的的某个订单
     */
    Result<Order> getOrderMessage(Long outTradeNo);

    /**
     * 获取订单列表
     */
    Result<PageResult<Order>> getOrderPage(Integer page);

    /**
     * 关闭交易
     */
    Result closeOrder(Long outTradeNo);

    /**
     * 管理员修改订单状态
     */
    Result updateOrderState(Long outTradeNo, String state);
}
