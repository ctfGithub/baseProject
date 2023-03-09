package com.springbootbasepackage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="登录参数")
public class UserDTO implements Serializable {
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String iphone;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "密码")
    private String password;
}
