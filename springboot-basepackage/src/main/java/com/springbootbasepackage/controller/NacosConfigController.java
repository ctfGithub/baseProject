package com.springbootbasepackage.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nacosConfig")
@RefreshScope
public class NacosConfigController {

    @Value("${app.name:defaultApp}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String appVersion;


    @GetMapping("/config")
    public String getConfig() {
        return "appName=" + appName
                + "\nappVersion=" + appVersion;
    }


}
