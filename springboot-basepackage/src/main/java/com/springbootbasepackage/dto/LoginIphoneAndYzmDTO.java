package com.springbootbasepackage.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="登录参数")
public class LoginIphoneAndYzmDTO {
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String iphone;


    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String yzm;

}
