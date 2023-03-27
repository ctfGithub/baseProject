package com.springbootbasepackage.exception;

import com.springbootbasepackage.enums.ErrorCodeEnum;
import lombok.Data;

/**
 * @author史良兵 @Date:2020/11/19 9:44 @Description: 业务异常类
 */
@Data
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private ErrorCodeEnum errorCodeEnum;

    public BusinessException(Integer code, String message) {
        super(message);
        setCode(code);
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.errorCodeEnum = errorCodeEnum;
        setCode(errorCodeEnum.getStatus());
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable ex) {
        super(ex);
    }
}
