package com.springbootbasepackage.controller;


import com.springbootbasepackage.base.SntResult;
import com.springbootbasepackage.dto.PartnerSourceDTO;
import com.springbootbasepackage.service.PartnerSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/partnerSourceController")
@RestController
public class PartnerSourceController {

    @Autowired
    private PartnerSourceService partnerSourceService;



    @GetMapping("/testPartnerSource")
    public List<PartnerSourceDTO> testPartnerSource (){
        List<PartnerSourceDTO> list  = partnerSourceService.testPartnerSource();
        return list;
    }

    @PostMapping("/save")
    public SntResult<Integer> save (@RequestBody PartnerSourceDTO partnerSourceDTO){
        Integer cnt  = partnerSourceService.save(partnerSourceDTO);
        return SntResult.ok(cnt);
    }

    @PostMapping("/testRedis")
    public SntResult<String> testRedis (){
        String cnt  = partnerSourceService.testRedis();
        return SntResult.ok(cnt);
    }



}
