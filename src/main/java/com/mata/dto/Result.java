package com.mata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Result <T> {
    private T data; // 数据

    private Integer code; // 响应代码 1成功 0失败

    private String message; // 消息

    /**
     *  返回成功
     */
    public static <T> Result<T> success(T data,String message){
        return new Result<>(data,1,message);
    }

    public static <T> Result<T> success(T data){
        return new Result<>(data,1,null);
    }

    public static Result success(String message){
        return new Result<>(null,1,message);
    }

    /**
     *  返回失败
     */
    public static <T> Result<T> error(String message){
        return new Result<>(null,0,message);
    }
}
