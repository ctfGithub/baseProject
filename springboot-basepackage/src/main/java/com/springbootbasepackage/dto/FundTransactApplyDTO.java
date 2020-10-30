package com.springbootbasepackage.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * <pre>
 * created by nvwa gen at 2020-02-27 10:13:20.853159
 * </per>
 *
 */
@Data
public class FundTransactApplyDTO  {

    private static final long serialVersionUID = 1L;

    /**
     * 钱包id
     */
    private String walletId;

    /**
     * 操作类型1充值2提现
     */
    private Integer applyType;

    /**
     * 审核状态 FundTransactApplyStatusEnum
     */
    private Integer applyStatus;
    private String applyStatusName;

    /**
     * 钱包类型1松鼠2伙伴
     */
    private Integer walletType;

    /**
     * 外部关联id
     */
    private String relationId;

    /**
     * 外部关联Name
     */
    private String relationName;
    /**
     * 外部关联id
     */
    private String bankInfo;
    /**
     * 付款名
     */
    private String payAccountName;

    /**
     * 付款银行账号
     */
    private String payAccount;

    /**
     * 付款银行
     */
    private String payBankName;

    /**
     * 付款银行支行
     */
    private String payBankSubName;

    /**
     * 收款方
     */
    private String receiveAccountName;

    /**
     * 收款银行账号
     */
    private String receiveAccount;

    /**
     * 收款银行
     */
    private String receiveBankName;

    /**
     * 收款银行支行
     */
    private String receiveBankSubName;

    /**
     * 收款银行行号
     */
    private String receiveBankCode;
    /**
     * 银行流水
     */
    private String bankFlowNo;

    /**
     * 余额
     */
    private String balance;

    /**
     * 待支付合同保证金
     */
    private BigDecimal unpaidBond;
    /**
     * 支付金额
     */
    private BigDecimal payMoney;

    /**
     * 实际支付金额
     */
    private BigDecimal realPayMoney;

    /**
     * 付款时间
     */
    private Date payTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 操作人
     */
    private String operator;

    /**
     *
     */
    private String gmtCreate;

    /**
     *
     */
    private String gmtModified;

    /**
     * 提现本金
     */
    private BigDecimal purchaseSettleAmount;

    /**
     * 提现服务费
     */
    private BigDecimal serviceAmount;
}
