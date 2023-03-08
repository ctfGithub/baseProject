package com.springbootbasepackage.controller;

import com.springbootbasepackage.base.SntResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class CommonController {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody//为了返回数据
    public SntResult index(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        //将返回信息写入response
        return SntResult.fail("500",ex.getMessage());
    }

}
