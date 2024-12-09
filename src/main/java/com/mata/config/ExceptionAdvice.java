package com.mata.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import com.mata.dto.Result;
import com.mata.exception.BusinessException;
import com.mata.exception.SystemException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@RestControllerAdvice
public class ExceptionAdvice {
    /**
     * 实体类验证拦截器
     */
    @ExceptionHandler(value = BindException.class)
    public Result<?> handleObject(BindException bindException) {
        BindingResult bindingResult = bindException.getBindingResult();
        return Result.error(bindingResult.getFieldError().getDefaultMessage());
    }

    /**
     * 单字段验证拦截器
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result<?> handleSingleField(ConstraintViolationException constraintViolationException) {
        Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
        // 返回第一个发现的错误
        return Result.error(constraintViolations.iterator().next().getMessage());
    }

    /**
     * 请求字段缺失拦截
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result<?> handleMissingServletRequestParameter(MissingServletRequestParameterException missingServletRequestParameterException) {
        return Result.error("请输入必要字段");
    }

    /**
     * 业务异常处理
     */
    @ExceptionHandler(value = BusinessException.class)
    public Result<?> handleBusinessException(BusinessException businessException){
        return Result.error(businessException.getMessage());
    }

    /**
     * 检查登录拦截器
     */
    @ExceptionHandler(value = NotLoginException.class)
    public Result<?> handleNotLoginException(NotLoginException notLoginException, HttpServletResponse response){
        response.setStatus(401);
        return Result.error("请先登录");
    }

    /**
     * 检查角色
     */
    // 拦截：缺少角色异常
    @ExceptionHandler(NotRoleException.class)
    public Result<?> handlerNotRoleException(NotRoleException e) {
        return Result.error("您没有权限");
    }


//    /**
//     * 系统异常处理
//     */
//    @ExceptionHandler(value = Exception.class)
//    public Result handleException(){
//        return Result.error("系统异常，请稍后再试");
//    }
//
//    /**
//     * 系统异常处理
//     */
//    @ExceptionHandler(value = SystemException.class)
//    public Result handleSystemException(SystemException systemException){
//        return Result.error(systemException.getMessage());
//    }
}
