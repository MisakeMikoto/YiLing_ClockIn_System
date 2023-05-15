package com.yiling.controller.exception;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author MisakiMikoto
 * @Date 2022/12/5 11:28
 */
public class BusinessException extends RuntimeException{
    private int code;


    public int getCode() {
        return code;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(int code, Throwable cause, String message) {
        super(message, cause);
        this.code = code;
    }

}
