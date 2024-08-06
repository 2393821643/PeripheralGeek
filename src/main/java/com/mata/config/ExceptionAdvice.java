package com.mata.config;

import com.mata.dto.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@RestControllerAdvice
public class ExceptionAdvice {
    /**
     * 实体类验证拦截器
     */
    @ExceptionHandler(value = BindException.class)
    public Result<?> handle(BindException bindException) {
        BindingResult bindingResult = bindException.getBindingResult();
        return Result.error(bindingResult.getFieldError().getDefaultMessage());
    }

    /**
     * 单字段验证拦截器
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result<?> handle(ConstraintViolationException constraintViolationException) {
        Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
        // 返回第一个发现的错误
        return Result.error(constraintViolations.iterator().next().getMessage());
    }
}
