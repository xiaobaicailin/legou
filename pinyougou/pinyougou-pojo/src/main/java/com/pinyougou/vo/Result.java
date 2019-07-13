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

    public static Result ok(){
        return new Result("成功","true");
    }

    public static Result fail(){
        return new Result("失败","false");
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
