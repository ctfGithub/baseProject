package com.springbootbasepackage.enums;


import com.springbootbasepackage.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author Andy.Tan
 * @Description: 错误返回枚举
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
    /**
     * 执行成功
     */
    SUCCESS(200, "执行成功"),
    /**
     * 服务异常
     */
    FAIL(999, "服务异常"),
    /**
     * 参数错误
     */
    ERROR_PARAM(771, "参数错误"),
    /**
     * 请求失效
     */
    REQ_EXPIRE(772, "请求失效"),
    /**
     * 未知异常
     */
    EXCEPT_UN_KNOW(773, "未知异常"),
    /**
     * 数据库操作异常
     */
    EXCEPT_DB_OPERATION(774, "数据库操作异常"),
    /**
     * 服务异常
     */
    SERVICE_EXCEPTION(775, "服务异常"),
    /**
     * 用户不存在
     */
    NO_EXISTS_USER(776, "用户不存在"),
    /**
     * 数据不存在
     */
    NO_EXISTS_DATA(777, "数据不存在"),
    /**
     * HTTP请求方法不支持
     */
    ERROR_METHOD_NOT_SUPPORT(779, "HTTP请求方法不支持"),
    /**
     * 用户认证失败
     */
    CERTIFICATION_FAILED(780, "用户认证失败"),
    /**
     * 找不到该用
     */
    NO_USER_INFO(781, "找不到该用户"),
    /**
     * 活动信息不存在
     */
    NO_ACT_INFO(782, "活动信息不存在"),
    /**
     * 活动商品明细不存在
     */
    NO_ACT_SKU_DETAIL(783, "活动商品明细不存在"),
    /**
     * 活动库存不存在
     */
    NO_ACT_STOCK(784, "活动库存不存在"),
    /**
     * 活动库存不足
     */
    ACT_STOCK_NOT_ENOUGH(785, "活动库存不足"),
    /**
     * 状态已改变
     */
    ERROR_ACT_STATUS(786, "状态已改变"),
    /**
     * 活动渠道明细不存在
     */
    NO_ACT_CHANNEL_DETAIL(787, "活动渠道明细不存在"),
    /**
     * 活动省市区县明细不存在
     */
    NO_ACT_ORG_AREA_DETAIL(788, "活动省市区县明细不存在"),
    /**
     * 活动进行中，不能撤回
     */
    NO_ACT_RECALL(789, "活动进行中，不能撤回"),
    /**
     * 不在活动范围时间内
     */
    NOTIN_ACT_EXTENT(790, "不在活动范围时间内"),

    /**
     * 活动时间已经过期
     */
    ACT_EXPRIE(791, "活动时间已经过期"),

    /**
     * 没有权限
     */
    NO_EXIST_ROLE(792, "没有权限"),

    /**
     * 售点查询入参不能为空
     */
    NO_EMPTY_POC_PARAM(793, "售点查询入参不能为空"),

    /**
     * 查询条件区域和渠道不能为空
     */
    NO_EMPTY_QUERY_PARAM(794, "查询条件区域和渠道不能为空"),

    /**
     * 活动参与的商品明细不可为空
     */
    NO_EMPTY_PRODUCT_INFO(795, "活动参与的商品明细不可为空"),

    /**
     * 促销购买商品档案SKU编码不可为空
     */
    NO_EMPTY_PURCHASE_BASE_SKU(796, "促销购买商品档案SKU编码不可为空"),

    /**
     * 促销赠品商品档案SKU编码不可为空
     */
    NO_EMPTY_GIVE_BASE_SKU(797, "促销赠品商品档案SKU编码不可为空"),

    /**
     * 买A赠A活动商品必需相同
     */
    IDENTICAL_BASE_SKU(798, "买A赠A活动商品必需相同"),

    /**
     * 买A赠A活动商品必需相同
     */
    DIFFERENT_BASE_SKU(799, "买A赠B活动商品不能相同"),

    /**
     * 促销购买商品购买数量不可为空
     */
    NO_EMPTY_PURCHASE_COUNT(800, "促销购买商品购买数量不可为空"),

    /**
     * 促销赠送商品购买数量不可为空
     */
    NO_EMPTY_GIVE_COUNT(801, "促销赠送商品购买数量不可为空"),

    /**
     * 商品在其他未结束活动中已存在
     */
    EXISTS_PRODUCT_ACT(802, "商品在其他未结束活动中已存在"),

    /**
     * 新增售点不能为空
     */
    NO_EMPTY_POC(803, "新增售点不能为空"),

    /**
     * 促销事件名称已存在
     */
    EXISTS_ACT_NAME(804, "促销事件名称已存在"),

    /**
     * 当前状态不能编辑活动
     */
    NO_UPDATE_ACT(805, "当前状态不能编辑活动"),

    /**
     * 活动开始时间不能晚于结束时间
     */
    ACT_TIME_ERROR(806, "活动开始时间不能晚于结束时间"),

    /**
     * 活动开始时间不能晚于结束时间
     */
    ACT_TIME_NOT_NULL(807, "活动时间不能为空"),

    /**
     * 促销赠送商品不能为空
     */
    NO_EMPTY_GIVE(808, "促销赠送商品不能为空"),
    /**
     * 参数不能为空
     */
    NO_EMPTY_PARAM(809, "参数不能为空"),
    /**
     * 商品重复选择
     */
    COMBINATION_DIFFERENT_BASE_SKU(810, "商品重复选择"),
    /**
     * 促销购买商品档案SKU名称不可为空
     */
    NO_EMPTY_PURCHASE_BASE_SKU_NAME(811, "促销购买商品档案SKU名称不可为空"),

    /**
     * 促销赠送商品档案SKU名称不可为空
     */
    NO_EMPTY_GIVE_BASE_SKU_NAME(812, "促销赠送商品档案SKU名称不可为空"),

    /**
     * 新增经销商不能为空
     */
    NO_EMPTY_WHOLESALER(813, "新增经销商不能为空"),

    /**
     * 商品服务异常
     */
    PRODUCT_SERVICE_EXCEPTION(814, "商品服务异常"),

    /**
     * BUSINESS-ENTITY服务异常
     */
    BUSINESS_ENTITY_SERVICE_EXCEPTION(815, "BUSINESS-ENTITY服务异常"),

    /**
     * 无匹配的经销商
     */
    NO_MATCH_WHOLESALER(816, "无匹配的经销商"),

    /**
     * 提交任务审核异常
     */
    COMMIT_BIZ_EXCEP(830, "提交任务审核-biz服务异常"),

    /**
     * 重新提交-biz服务异常
     */
    AUDIT_RESUBIT(831, "重新提交-biz服务异常"),

    /**
     * 经销商列表ID重复
     */
    WHOLESALER_ID_REPEAT(817, "经销商列表ID重复"),


    LM_CALLBACK_ERROR(818, "回调地址配置错误"),
    LM_TOKEN_ERROR(819, "TOKEN无效"),
    LM_EXT_FIELD_GENERATE_ERROR(820, "扩展字段生成错误"),
    LM_EXT_FIELD_GET_ERROR(821, "扩展字段解析错误"),
    LM_IMG_ERROR(822, "图片异常，请稍后再试"),
    LM_SYSTEM_ERROR(823, "系统异常，请稍后再试"),
    LM_EMPTY_RESPONSE_ERROR(899, "系统异常，请稍后再试"),


    CAN_NOT_PARTICIPATION_THIS_ACTIVITY(900, "您暂时无法参与该活动"),
    /**
     * 状态已改变
     */
    ERROR_ACTIVITY_STATUS(1001, "“未开始”和“进行中”的数据才可以操作置顶"),

    /**
     * 导出相关错误码
     */
    EXPORT_EXCEL_EXCEPTION(2001, "导出excel异常"),
    /**
     * 无促销管理权限
     */
    PERMISSION_FAIL(403, "无促销管理权限");


    private Integer status;

    private String message;

    private static final Set<ErrorCodeEnum> ALL = EnumSet.allOf(ErrorCodeEnum.class);

    public static ErrorCodeEnum getCodeByName(Integer code) {
        if (code == null) {
            return null;
        }

        return ALL.stream()
                .filter(o -> o.status.equals(code))
                .findAny().orElseThrow(() -> new BusinessException("错误编码枚举类: " + code + "未匹配到相关值！"));

    }

    public boolean is(Integer code) {
        return getStatus().equals(code);
    }

}
