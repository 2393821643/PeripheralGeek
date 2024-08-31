package com.mata.utils;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import static com.alipay.api.AlipayConstants.CHARSET_UTF8;
import static com.alipay.api.AlipayConstants.SIGN_TYPE_RSA2;

@Component
@Slf4j
public class AlipayUtil {
    @Autowired
    private AlipayClient alipayClient;

    @Value("${zfb.payNoticeUrl}")
    private String payNoticeUrl;

    @Value("${zfb.alipayPublicKey}")
    private String alipayPublicKey;


    /**
     * 创建交易 返回支付的页面信息
     *
     * @param outTradeNo 商家唯一交易号
     * @param cost       价格
     * @param goodsName  商品名称
     * @return pageCode 付款页面代码
     */
    public String createOrder(String outTradeNo, BigDecimal cost, String goodsName) {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        // 设置商户订单号
        model.setOutTradeNo(outTradeNo);
        // 设置订单标题
        model.setSubject(goodsName);
        // 设置订单总金额
        model.setTotalAmount(cost.toString());
        // 销售产品码 固定值，别改
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        // 装载模型
        request.setBizModel(model);
        request.setNotifyUrl(payNoticeUrl);
        try {
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "POST");
            String pageCode = response.getBody();
            // System.out.println(pageCode);
            return pageCode;
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 统一收单交易查询 获取交易状态
     *
     * @param outTradeNo 商家唯一交易号
     */
    public String getPayState(String outTradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        // 要查询的 商家唯一交易号
        model.setOutTradeNo(outTradeNo);
        request.setBizModel(model);
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            // 返回状态 状态字段看文档
            return response.getTradeStatus();
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 统一收单交易退款
     *
     * @param outRequestNo 商家自定义退款号
     * @param outTradeNo   商家唯一交易号
     * @param refundAmount 退款金额
     * @retrun 返回true/false 退款成功/失败
     */
    public boolean refund(String outRequestNo, String outTradeNo, String refundAmount) {
        // 先查看订单状态
        String payState = getPayState(outTradeNo);
        if (!Objects.equals(payState, "TRADE_SUCCESS")) {
            return false;
        }
        // TODO 根据outTradeNo查 收益金额和退款金额
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(outTradeNo);
        model.setOutRequestNo(outRequestNo);
        model.setRefundAmount(refundAmount);
        request.setBizModel(model);
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            // 本次退款是否发生了资金变化
            String fundChange = response.getFundChange();
            return fundChange.equals("Y");

        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 退款查询
     *
     * @param outRequestNo 商家自定义退款号
     * @param outTradeNo   商家唯一交易号
     */
    public String getRefundState(String outRequestNo, String outTradeNo) {
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();

        model.setOutTradeNo(outTradeNo);
        model.setOutRequestNo(outRequestNo);

        request.setBizModel(model);
        try {
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            return response.getRefundStatus();
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 交易关闭
     *
     * @param outTradeNo 商家唯一交易号
     */
    public boolean closePay(String outTradeNo) {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        // 将{"out_trade_no":"xxx"}化为字符串
        JSON json = new JSONObject();
        json.putByPath("out_trade_no", outTradeNo);
        String jsonStr = JSONUtil.toJsonStr(json);

        request.setBizContent(jsonStr);
        try {
            AlipayTradeCloseResponse response = alipayClient.execute(request);
            return response.isSuccess();
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 回调接口验证
     */
    public boolean verify(Map<String, String> paramsMap) {
        try {
            return AlipaySignature.rsaCheckV1(paramsMap, alipayPublicKey, CHARSET_UTF8, SIGN_TYPE_RSA2);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }


}