package com.springbootbasepackage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ActivityImportWholesaleDto {

    @ApiModelProperty(value = "成功数")
    private Integer successNum;

    @ApiModelProperty(value = "失败数")
    private Integer failNum;

    @ApiModelProperty(value = "失败地址")
    private String failOssUrl;


}
