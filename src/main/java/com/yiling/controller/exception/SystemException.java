package com.yiling.controller.exception;

/**
 * @Author MisakiMikoto
 * @Date 2022/12/5 11:28
 */
public class SystemException extends RuntimeException{
    private int code;


    public int getCode() {
        return code;
    }

    public SystemException(int code, String message) {
        super(message);
        this.code = code;
    }

    public SystemException(int code, Throwable cause, String message) {
        super(message, cause);
        this.code = code;
    }
}
