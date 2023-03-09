package com.springbootbasepackage.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="发送验证码手机号")
public class LoginIphoneDTO {
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String iphone;


}
