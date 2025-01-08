package com.mata.model.order.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.mata.model.order.dto.BuyMessageDto;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
import com.mata.model.order.dto.OrderConditionDto;
import com.mata.model.order.dto.OrderShipmentDto;
import com.mata.pojo.Order;
import com.mata.model.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 购买商品，返回支付订单号
     */
    @PostMapping("/buy/{goodsId}")
    @SaCheckLogin
    @SaCheckRole("user")
    public Result<String> buyGoods(@PathVariable("goodsId") Long goodsId, @RequestBody @Validated BuyMessageDto buyMessageDto) {
        return orderService.buyGoods(goodsId, buyMessageDto);
    }

    /**
     * 支付/退款回调接口
     */
    @PostMapping("/notice")
    public void payNotice(HttpServletRequest httpServletRequest) {
        orderService.payNotice(httpServletRequest);
    }

    /**
     * 继续支付 生成支付订单html
     */
    @PostMapping("/buy/continue/{outTradeNo}")
    @SaCheckLogin
    @SaCheckRole("user")
    public Result<String> continuePay(@PathVariable("outTradeNo") Long outTradeNo) {
        return orderService.continuePay(outTradeNo);
    }

    /**
     * 查看订单信息 只能看当前账号的的某个订单
     */
    @GetMapping("/{outTradeNo}")
    @SaCheckLogin
    @SaCheckRole("user")
    public Result<Order> getOrderMessage(@PathVariable("outTradeNo") Long outTradeNo) {
        return orderService.getOrderMessage(outTradeNo);
    }

    /**
     * 获取订单列表
     * @param page:页数
     * @param state: 查询条件 1：所有订单/2：未支付订单/3：待发货/4：已完成
     */
    @GetMapping("/list/{page}/{state}")
    @SaCheckLogin
    @SaCheckRole("user")
    public Result<PageResult<Order>> getOrderPage(@PathVariable("page") Integer page,@PathVariable("state")Integer state) {
        return orderService.getOrderPage(page,state);
    }

    /**
     * 关闭交易
     */
    @DeleteMapping("/close/{outTradeNo}")
    @SaCheckLogin
    @SaCheckRole("user")
    public Result closeOrder(@PathVariable("outTradeNo") Long outTradeNo) {
        return orderService.closeOrder(outTradeNo);
    }

    /**
     * 管理员修改订单状态
     */
    @PutMapping("/admin/shipments")
    @SaCheckLogin
    @SaCheckRole("admin")
    public Result updateOrderState(@RequestBody @Validated OrderShipmentDto orderShipmentDto) {
        return orderService.updateOrderState(orderShipmentDto);
    }

    /**
     * 管理员查找订单列表
     */
    @PostMapping("/admin")
    @SaCheckLogin
    @SaCheckRole("admin")
    public Result<PageResult<Order>> getAdminOrderPage(@RequestBody OrderConditionDto orderConditionDto){
        return orderService.getAdminOrderPage(orderConditionDto);
    }
}
