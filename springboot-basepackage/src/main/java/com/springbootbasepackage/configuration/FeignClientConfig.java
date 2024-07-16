package com.springbootbasepackage.configuration;

import feign.Feign;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {
    @Bean
    public RequestInterceptor customRequestInterceptor() {
        return new RequestIdInterceptor();
    }

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder().requestInterceptor(new RequestIdInterceptor()); // 注册拦截器
    }
}
