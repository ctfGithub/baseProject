package com.springbootbasepackage.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * <pre>
 * created by nvwa gen at 2020-02-27 10:13:20.859201
 * </per>
 *
 */
@Data
public class FundTransactApplyQuery {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 钱包id
     */
    private String walletId;

    /**
     * 操作类型1充值2提现
     */
    private Integer applyType;

    /**
     * 审核状态
     */
    private Integer applyStatus;

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
     * 待支付合同保证金
     */
    private BigDecimal unpaidBond;

    /**
     * 余额
     */
    private String balance;

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
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 业务帐套
     */
    private Integer bizType;

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
     * 开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date payStartTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date payEndTime;
    /**
     * 小于开始时间
     */
    private Date lessStartTime;

    /**
     * 大于结束时间
     */
    private Date moreEndTime;

    private Boolean isLimit=true;
    private String idList;
    private String [] ids;
}
