package com.sekai.game2048.model;

public class Result<T> {

    private String code;
    private String message;
    private boolean success;
    private T data;

    public static <T> Result<T> ok(T data, String message) {
        Result<T> result = new Result<>();
        result.setCode("200");
        result.setMessage(message);
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(String message) {
        Result<T> result = new Result<>();
        result.setCode("400");
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
