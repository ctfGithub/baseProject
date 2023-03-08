package com.springbootbasepackage.base;


public class SntException extends RuntimeException {
    private String message;
    private String code;

    public SntException() {
    }

    public SntException(String message) {
        super(message);
        this.message = message;
    }

    public SntException(String code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public SntException(String message, String code, Throwable e) {
        super(message, e);
        this.code = code;
        this.message = message;
    }

    public SntException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public SntException(Throwable e) {
        super(e);
    }

    public String getMsg() {
        return this.message;
    }

    public String getCode() {
        return this.code;
    }
}
