package com.springbootbasepackage.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserDO {

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String iphoneNo;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "密码")
    private String passWord;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;


}
