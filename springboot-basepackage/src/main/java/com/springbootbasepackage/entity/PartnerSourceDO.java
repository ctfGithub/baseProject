package com.springbootbasepackage.entity;

import lombok.Data;

/**
 *
 * <pre>
 * created by nvwa gen at 2020-09-05 10:02:59.842626
 * </per>
 *
 */
@Data
public class PartnerSourceDO  {

    /**
     *主键
     */
    private Long id;

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
    private Integer partnerScore;

    /**
     * 是否冻结，0|否，1|是
     */
    private Integer freezeFlag;

    /**
     * 伙伴id
     */
    private String partnerId;

    /**
     * 伙伴名称
     */
    private String partnerName;

    /**
     * 是否签署协议，0|否，1|是
     */
    private Integer agreementFlag;

    /**
     * 保利协议标识：0|未签署，1|已签署
     */
    private Integer factoringAgreementFlag;

    /**
     * 保理协议签订后的url
     */
    private String factoringAgreementUrl;


    /**
     * 授权协议url
     */
    private String agreementUrl;


    private String extAtt;

}

