package com.springbootbasepackage.base;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SntResult<T> implements Serializable {
    private static final long serialVersionUID = 2994494589314183496L;
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3-9][0-9]{9}$");
    private boolean success;
    private T data;
    private BaseQuery query;
    private String errCode;
    private String errMsg;

    public SntResult() {
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseQuery getQuery() {
        return this.query;
    }

    public void setQuery(BaseQuery query) {
        this.query = query;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public static <T> SntResult ok(T data, BaseQuery query) {
        SntResult<T> result = new SntResult();
        result.setData(data);
        result.setQuery(query);
        result.setSuccess(true);
        return result;
    }

    public static <T> SntResult<T> ok() {
        return (SntResult<T>) ok((Object)null);
    }

    public static <T> SntResult<T> ok(T data) {
        return ok(data, (BaseQuery)null);
    }

    public static <T> SntResult<T> okOfFormat(T data, BaseQuery query) {
        return ok(format(data, false), query);
    }

    public static <T> SntResult<T> okOfFormat(T data) {
        return okOfFormat(data, (BaseQuery)null);
    }

    public static <T> SntResult<T> okOfFormat(T data, boolean desensitization, BaseQuery query) {
        return ok(format(data, desensitization), query);
    }

    public static <T> SntResult<T> okOfFormat(T data, boolean desensitization) {
        return okOfFormat(data, desensitization, (BaseQuery)null);
    }

    public static <T> SntResult<T> fail(String errCode, String errMsg) {
        SntResult result = new SntResult();
        result.setSuccess(false);
        result.setErrCode(errCode);
        result.setErrMsg(errMsg);
        return result;
    }

    public static <T> SntResult<T> fail(SntException e) {
        return fail(e.getCode(), e.getMsg());
    }

    private static <T> Object format(T data, boolean desensitization) {
        if (data == null) {
            return null;
        }else if (data instanceof List && ((List)data).size() > 0) {
            List list = new ArrayList(((List)data).size());
            ((List)data).forEach((d) -> {
                if (null == d) {
                    list.add(d);
                } else if (d instanceof List) {
                    list.add(format(d, desensitization));
                } else {
                    list.add(d);
                }

            });
            return list;
        } else {
            return data;
        }
    }

}
