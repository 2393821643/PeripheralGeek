package com.mata.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.alipay.api.AlipayConstants.*;

@Configuration
public class ZfbConfig {
    @Value("${zfb.url}")
    private String url;

    @Value("${zfb.appId}")
    private String appId;

    @Value("${zfb.privateKey}")
    private String privateKey;

    @Value("${zfb.encryptKey}")
    private String encryptKey;

    @Value("${zfb.alipayPublicKey}")
    private String alipayPublicKey;


    @Bean
    public AlipayClient alipayClient(){
        AlipayConfig alipayConfig = new AlipayConfig();
        //设置网关地址
        alipayConfig.setServerUrl(url);
        //设置应用ID
        alipayConfig.setAppId(appId);
        // 设置应用公钥
        alipayConfig.setEncryptKey(encryptKey);
        //设置应用私钥
        alipayConfig.setPrivateKey(privateKey);
        // 设置支付宝公钥
        alipayConfig.setAlipayPublicKey(alipayPublicKey);
        //设置请求格式，固定值json
        alipayConfig.setFormat(FORMAT_JSON);
        //设置字符集
        alipayConfig.setCharset(CHARSET_UTF8);
        //设置签名类型
        alipayConfig.setSignType(SIGN_TYPE_RSA2);
        //实例化客户端
        try {
            return new DefaultAlipayClient(alipayConfig);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }
}
