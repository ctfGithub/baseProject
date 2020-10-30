package com.springbootbasepackage.exception;


import com.springbootbasepackage.constant.UfcErrEnum;


public class UfcException extends SntException {

    public UfcException(String code, String msg, Throwable e) {
        super(msg, code, e);
    }
    public UfcException(UfcErrEnum errEnum, Throwable e) {
        this(errEnum.getCode(), errEnum.getMsg(), e);
    }

    public UfcException(UfcErrEnum errEnum) {
        super(errEnum.getCode(), errEnum.getMsg());
    }
    public UfcException(UfcErrEnum errEnum, Object...args) {
        super(errEnum.getCode(), String.format(errEnum.getMsg(),args));
    }
    public UfcException(String code, String msg){
        super(code,msg);
    }
}
