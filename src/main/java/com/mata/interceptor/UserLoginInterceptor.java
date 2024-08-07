package com.mata.interceptor;

import cn.hutool.core.util.StrUtil;
import com.mata.enumPackage.Role;
import com.mata.exception.BusinessException;
import com.mata.holder.Holder;
import com.mata.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserLoginInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1:获取请求体的token
        String token = request.getHeader("Authorization");
        //1.1: 判断token是否为空
        if (StrUtil.isBlank(token)) {
            response.setStatus(401);
            return false;
        }
        // 解析token 获取id
        Integer id = jwtUtil.parseToken(token, Role.User);
        if (id == null){
            response.setStatus(401);
            return false;
        }
        Holder.saveUser(id);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移除用户
        Holder.removeUser();
    }
}
