package com.mata.model.order.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.mata.model.order.dto.BuyMessageDto;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
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
     */
    @GetMapping("/list/{page}")
    @SaCheckLogin
    @SaCheckRole("user")
    public Result<PageResult<Order>> getOrderPage(@PathVariable("page") Integer page) {
        return orderService.getOrderPage(page);
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
    @PutMapping("/admin/{outTradeNo}")
    @SaCheckLogin
    @SaCheckRole("admin")
    public Result updateOrderState(@PathVariable("outTradeNo") Long outTradeNo, @RequestParam("state") String state) {
        return orderService.updateOrderState(outTradeNo, state);
    }
}
