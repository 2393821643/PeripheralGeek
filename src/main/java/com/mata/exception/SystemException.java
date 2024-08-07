package com.mata.exception;

//建立自定义异常 业务异常
public class SystemException extends Exception {
    //继承RuntimeException（运行时异常）
    public SystemException(String message) {
        super(message);
    }


}
