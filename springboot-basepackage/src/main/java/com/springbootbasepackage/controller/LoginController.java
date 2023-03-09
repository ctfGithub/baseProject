package com.springbootbasepackage.controller;

import com.springbootbasepackage.base.SntResult;
import com.springbootbasepackage.dto.LoginIphoneDTO;
import com.springbootbasepackage.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PostMapping("/sendIphone")
    @ApiOperation("发送手机号生成验证码")
    public SntResult<String> sendIphone (@RequestBody LoginIphoneDTO loginIphoneDTO){
        String token  = loginService.sendIphone(loginIphoneDTO);
        return SntResult.ok(token);
    }

}
