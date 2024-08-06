package com.mata.exception;

//建立自定义异常 业务异常
public class BusinessException extends RuntimeException {
    //继承RuntimeException（运行时异常）
    public BusinessException(String message) {
        super(message);
    }


}
