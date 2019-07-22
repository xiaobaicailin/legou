package com.pinyougou.vo;

public class Result {
    private String message;
    private String success;

    public Result() {
    }

    public Result(String message, String success) {
        this.message = message;
        this.success = success;
    }

    public static Result ok(String msg){
        return new Result(msg,"true");
    }

    public static Result fail(String msg){
        return new Result(msg,"false");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
