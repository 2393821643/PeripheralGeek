package com.mata.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.mata.dto.BuyMessageDto;
import com.mata.dto.PageResult;
import com.mata.dto.Result;
import com.mata.pojo.Order;
import com.mata.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     *  购买商品，返回支付html
     */
    @PostMapping("/buy/{goodsId}")
    public Result<String> buyGoods(@PathVariable("goodsId") Long goodsId, @RequestBody @Validated BuyMessageDto buyMessageDto){
        return orderService.buyGoods(goodsId,buyMessageDto);
    }

    /**
     * 支付/退款回调接口
     */
    @PostMapping("/notice")
    public void payNotice(HttpServletRequest httpServletRequest){
        orderService.payNotice(httpServletRequest);
    }

    /**
     * 继续支付
     */
    @PostMapping("/buy/continue/{outTradeNo}")
    public Result<String> continuePay(@PathVariable("outTradeNo") Long outTradeNo){
        return orderService.continuePay(outTradeNo);
    }

    /**
     * 查看订单信息 只能看当前账号的的某个订单
     */
    @GetMapping("/{outTradeNo}")
    public Result<Order> getOrderMessage(@PathVariable("outTradeNo") Long outTradeNo){
        return orderService.getOrderMessage(outTradeNo);
    }

    /**
     * 获取订单列表
     */
    @GetMapping("/list/{page}")
    public Result<PageResult<Order>> getOrderPage(@PathVariable("page")Integer page){
        return orderService.getOrderPage(page);
    }

    /**
     * 关闭交易
     */
    @DeleteMapping("/close/{outTradeNo}")
    public Result closeOrder(@PathVariable("outTradeNo") Long outTradeNo){
        return orderService.closeOrder(outTradeNo);
    }

    /**
     * 管理员修改订单状态
     */
    @PutMapping("/admin/{outTradeNo}")
    public Result updateOrderState(@PathVariable("outTradeNo")Long outTradeNo,@RequestParam("state")String state){
        return orderService.updateOrderState(outTradeNo,state);
    }
}
