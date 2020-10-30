package com.springbootbasepackage.base;


public class SntException extends RuntimeException {
    private String msg;
    private String code;

    public SntException() {
    }

    public SntException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public SntException(String code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public SntException(String msg, String code, Throwable e) {
        super(msg, e);
        this.code = code;
        this.msg = msg;
    }

    public SntException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public SntException(Throwable e) {
        super(e);
    }

    public String getMsg() {
        return this.msg;
    }

    public String getCode() {
        return this.code;
    }
}
