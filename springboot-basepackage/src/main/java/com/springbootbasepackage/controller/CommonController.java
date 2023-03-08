package com.springbootbasepackage.controller;

import com.springbootbasepackage.base.SntException;
import com.springbootbasepackage.base.SntResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CommonController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(SntException.class)
    @ResponseBody//为了返回数据
    public SntResult handleException(SntException ex) {
        //将返回信息写入response
        logger.warn("业务异常 {}", ex.getMessage());
        logger.error("BusinessException {}", ex.getMessage(), ex);
        return SntResult.fail("",ex.getMessage());
    }
}
