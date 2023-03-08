package com.springbootbasepackage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *
 * <pre>
 * created by nvwa gen at 2020-09-05 10:02:59.855362
 * </per>
 *
 */
@Data
@ApiModel(value="伙伴得分-删除")
public class PartnerSourceDeleteDTO {


    @ApiModelProperty(value = "主键id")
    private Long id;

}
