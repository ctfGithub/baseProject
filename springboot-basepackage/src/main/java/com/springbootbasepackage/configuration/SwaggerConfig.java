package com.springbootbasepackage.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
        return new ApiInfoBuilder()
                .title("使用接口文档")
                .version("1.0.0")
                .build();
    }


}
