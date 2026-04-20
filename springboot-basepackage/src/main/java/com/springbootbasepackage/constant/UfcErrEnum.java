package com.springbootbasepackage.constant;


/**
 * @author stl
 */
public enum UfcErrEnum {
    /**
     * 必填字段消息
     */
    REQUIRED("10000", "[%s]缺失,请补全"),
    /**
     * 通用消息
     */
    COMMON("10001","%s"),

    /**
     * 接口报错
     */
    INTERFACE_WRING("20001","%s"),

    /**
     * 接口报错--节假日
     */
    INTERFACE_HOLIDY_WRING("30001","%s"),

    /**
     * 无数据
     */
    DATA_NOT_FIND("10002","[%s]没有对应记录"),

    SQUIRREL_PREPARE_ORDER_COUNT_LIMIT("10003","需要获取代偿结果的订单超出限制"),

    /**
     * 当前状态不允许审核
     */
    AUDITING_NO_PERMISSION("10004","当前状态不允许审核"),

    /**
     * 当前状态不允许金贝申请调整
     */
    LOAN_APPLY_ADJUST_NO_PERMISSION("10005","当前有申请在审核中，不能发起新的测算调整"),

    /**
     * 数据已存在
     */
    DATA_ALREADY_EXIST("10006","[%s]数据已存在"),

    /**
     * 当前状态不允许
     */
    CURRENT_STATUS_NO_PERMISSION("10007","当前状态不允许[%s]"),

    /**
     * 不能发起新的测算
     */
    LOAN_APPLY_AUDIT_NO_PERMISSION("10005","当前有申请在审核中，不能发起新的测算"),


    /**
     * 开店时间为空
     */
    OPEN_TIME_IS_NULL("10008","[%s]开店时间为空"),



    ;
    private final String code;
    private final String msg;

    UfcErrEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
