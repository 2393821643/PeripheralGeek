package com.mata.config;

import com.mata.interceptor.AdminLoginInterceptor;
import com.mata.interceptor.UserLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private UserLoginInterceptor userLoginInterceptor;

    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;
    static final String ORIGINS[] = new String[]{"GET", "POST", "PUT", "DELETE","OPTIONS"};

    /**
     * CORS
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有的当前站点的请求地址，都支持跨域访问。
                .allowedOriginPatterns("*") // 所有的外部域都可跨域访问。 如果是localhost则很难配置，因为在跨域请求的时候，外部域的解析可能是localhost、127.0.0.1、主机名
                .allowCredentials(true) // 是否支持跨域用户凭证
                .allowedMethods(ORIGINS) // 当前站点支持的跨域请求类型是什么
                .maxAge(3600); // 超时时长设置为1小时。 时间单位是秒。
    }

    /**
     * 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInterceptor)
                .addPathPatterns("/user/**")
                .addPathPatterns("/order/**")
                .addPathPatterns("/article/user/**")
                .excludePathPatterns("/user/information/**")
                .excludePathPatterns("/order/notice")
                .excludePathPatterns("/order/admin/**");
        registry.addInterceptor(adminLoginInterceptor)
                .addPathPatterns("/admin/**")
                .addPathPatterns("/order/admin/**")
                .addPathPatterns("/goods/admin/**")
                .addPathPatterns("/article/admin/**");
    }
}
