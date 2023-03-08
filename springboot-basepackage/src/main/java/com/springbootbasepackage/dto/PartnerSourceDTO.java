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
@ApiModel(value="伙伴得分")
public class PartnerSourceDTO {

    /**
     * 
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 扩展字段
     */
    private String extAtt;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 伙伴得分
     */
    @ApiModelProperty(value = "伙伴得分")
    private Integer partnerScore;

    /**
     * 是否冻结，0|否，1|是
     */
    @ApiModelProperty(value = "是否冻结，0|否，1|是")
    private Integer freezeFlag;

    /**
     * 伙伴id
     */
    @ApiModelProperty(value = "伙伴id")
    private String partnerId;

    /**
     * 伙伴名称
     */
    @ApiModelProperty(value = "伙伴名称")
    private String partnerName;

    /**
     * 是否签署协议，0|否，1|是
     */
    @ApiModelProperty(value = "是否签署协议，0|否，1|是")
    private Integer agreementFlag;

}
