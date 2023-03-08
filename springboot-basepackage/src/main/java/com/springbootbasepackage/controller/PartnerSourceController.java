package com.springbootbasepackage.controller;


import com.springbootbasepackage.base.SntResult;
import com.springbootbasepackage.dto.PartnerSourceDTO;
import com.springbootbasepackage.dto.PartnerSourceDeleteDTO;
import com.springbootbasepackage.service.PartnerSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/partnerSourceController")
@RestController
@Api(tags ="伙伴得分信息")
public class PartnerSourceController {

    @Autowired
    private PartnerSourceService partnerSourceService;

    @GetMapping("/List")
    @ApiOperation("伙伴得分列表")
    public List<PartnerSourceDTO> testPartnerSource (){
        List<PartnerSourceDTO> list  = partnerSourceService.testPartnerSource();
        return list;
    }

    @PostMapping("/save")
    @ApiOperation("保存伙伴得分")
    public SntResult<Integer> save (@RequestBody PartnerSourceDTO partnerSourceDTO){
        Integer cnt  = partnerSourceService.save(partnerSourceDTO);
        return SntResult.ok(cnt);
    }

    @PostMapping("/update")
    @ApiOperation("更新保存伙伴得分")
    public SntResult<Integer> update (@RequestBody PartnerSourceDTO partnerSourceDTO){
        Integer cnt  = partnerSourceService.update(partnerSourceDTO);
        return SntResult.ok(cnt);
    }

    @PostMapping("/delete")
    @ApiOperation("删除保存伙伴得分")
    public SntResult delete (@RequestBody PartnerSourceDeleteDTO partnerSourceDTO){
        Integer cnt  = partnerSourceService.delete(partnerSourceDTO);
        return SntResult.ok(cnt);
    }

    @PostMapping("/testRedis")
    @ApiOperation("账号生成")
    public SntResult<String> testRedis (){
        String cnt  = partnerSourceService.testRedis();
        return SntResult.ok(cnt);
    }



}
