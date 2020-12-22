package com.asahih.sms.result;

/**
 * Created by Asahi Huang on 2020/9/15.
 *
 * 构造响应码
 */
public class Result {
    //响应码
    private int code;
    private String message;
    private Object result;

    public Result(int code) {
        this.code = code;
    }

    Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.result = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}

