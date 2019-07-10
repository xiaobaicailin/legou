package com.pinyougou.vo;

public class Result {
    private String message;
    private String successful;

    public Result() {
    }

    public Result(String message, String successful) {
        this.message = message;
        this.successful = successful;
    }

    public static Result ok(){
        return new Result("添加成功","true");
    }

    public static Result fail(){
        return new Result("添加失败","false");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccessful() {
        return successful;
    }

    public void setSuccessful(String successful) {
        this.successful = successful;
    }
}
