package com.springbootbasepackage.controller;


import com.springbootbasepackage.base.SntResult;
import com.springbootbasepackage.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/testController")
@RestController
@Api(tags ="测试类")
public class TestController {

    @Autowired
    private TestService testService;



    @PostMapping("/ceshi1")
    @ApiOperation("数据校验")
    public SntResult<Integer> ceshi1 (){
        Integer cnt  = testService.ceshi1();
        return SntResult.ok(cnt);
    }


}
