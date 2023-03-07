package com.springbootbasepackage.configuration;

import springfox.documentation.service.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration  //注解添加Config到SpringBoot中
@EnableSwagger2  //开启Swagger2
public class SwaggerConfig implements WebMvcConfigurer {
    //配置Swagger的Docket的Bean实例
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //RequestHandlerSelectors,配置要扫描接口的方式，basePackage说明扫描包
                .apis(RequestHandlerSelectors.basePackage("com.springbootbasepackage.controller"))
                .build();
    }
    //配置Swagger 信息 --》 apiInfo
    private ApiInfo apiInfo(){

        //作者信息
        Contact contact = new Contact("hwh", "https://blog.csdn.net/kelekele111?type=blog", "123");

        return new ApiInfo(
                "哈哈哈哈",
                "Api----Swagger----Demo",
                "1.0",
                "https://blog.csdn.net/kelekele111?type=blog",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }


}
