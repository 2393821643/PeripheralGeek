package com.mata.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.mata.dto.BuyMessageDto;
import com.mata.dto.Result;
import com.mata.pojo.Order;
import com.mata.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
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
}
