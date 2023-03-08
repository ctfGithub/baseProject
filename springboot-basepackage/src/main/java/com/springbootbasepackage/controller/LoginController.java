package com.springbootbasepackage.controller;

import com.springbootbasepackage.base.SntResult;
import com.springbootbasepackage.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/loginController")
@RestController
@Api(tags ="登录")
public class LoginController {

    @Resource
    private LoginService loginService;

    //1、发送手机号 ，生产验证码


    //



    //2、手机号和 验证码生成token
    @PostMapping("/tokenCreate")
    @ApiOperation("登录")
    public SntResult<String> tokenCreate (){
        String token  = loginService.tokenCreate();
        return SntResult.ok(token);
    }


    //3、拦截器验证 token 的有效期和 白名单url请求 ，不校验 token




}
