package com.mata.model.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.model.order.dto.BuyMessageDto;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
import com.mata.model.order.dto.OrderConditionDto;
import com.mata.model.order.dto.OrderShipmentDto;
import com.mata.pojo.Order;

import javax.servlet.http.HttpServletRequest;

public interface OrderService extends IService<Order> {
    /**
     *  返回支付订单号
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
     * 继续支付 生成支付订单html
     */
    Result<String> continuePay(Long outTradeNo);

    /**
     * 查看订单信息 只能看当前账号的的某个订单
     */
    Result<Order> getOrderMessage(Long outTradeNo);

    /**
     * 获取订单列表
     * @param page:页数
     * @param state: 查询条件 1：所有订单/2：未支付订单/3：待发货/4：已完成
     */
    Result<PageResult<Order>> getOrderPage(Integer page,Integer state);

    /**
     * 关闭交易
     */
    Result closeOrder(Long outTradeNo);

    /**
     * 管理员修改订单状态
     */
    Result updateOrderState(OrderShipmentDto orderShipmentDto);

    /**
     * 发送邮箱 提示发货成功
     */
    void sendEmailOrderMessage(Long outTradeNo);

    /**
     * 管理员查找订单列表
     */
    Result<PageResult<Order>> getAdminOrderPage(OrderConditionDto orderConditionDto);
}
