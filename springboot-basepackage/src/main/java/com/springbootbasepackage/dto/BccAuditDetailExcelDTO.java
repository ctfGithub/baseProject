package com.springbootbasepackage.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 查询BCC审核详情出参
 *
 * @ClassName BccAuditRes
 * @Author yangrui
 * @Date 2022/9/31 22:10
 */
@Data
@ApiModel(value = "BccAuditExcelVo对象", description = "BccAuditExcelVo出参")
public class BccAuditDetailExcelDTO {

    @ApiModelProperty(value = "ID")
    @ExcelProperty(value = "ID",index = 0)
    @ColumnWidth(value = 30)
    private Long id;

    @ApiModelProperty(value = "审核状态")
    @ExcelProperty(value = "审核状态",index = 1)
    @ColumnWidth(value = 30)
    private String approveStatusName;

    @ApiModelProperty(value = "售点 ABI ONE ID")
    @ExcelProperty(value = "售点 ABI ONE ID",index = 2)
    @ColumnWidth(value = 30)
    private String abiOneId;

    @ApiModelProperty(value = "高德POI ID")
    @ExcelProperty(value = "高德POI ID",index = 3)
    @ColumnWidth(value = 30)
    private String poiId;

    @ApiModelProperty(value = "售点名称")
    @ExcelProperty(value = "售点名称",index = 4)
    @ColumnWidth(value = 30)
    private String pocName;

    @ApiModelProperty(value = "售点地址")
    @ExcelProperty(value = "售点地址",index = 5)
    @ColumnWidth(value = 30)
    private String pocAddress;

    @ApiModelProperty(value = "匹配方式")
    @ExcelProperty(value = "匹配方式",index = 6)
    @ColumnWidth(value = 30)
    private String pocCreateStatusName;

    @ApiModelProperty(value = "售点自定义名称")
    @ExcelProperty(value = "售点自定义名称",index = 7)
    @ColumnWidth(value = 30)
    private String pocSelfDefinedName;

    @ApiModelProperty(value = "BU")
    @ExcelProperty(value = "BU",index = 8)
    @ColumnWidth(value = 30)
    private String buName;

    @ApiModelProperty(value = "REGION")
    @ExcelProperty(value = "REGION",index = 9)
    @ColumnWidth(value = 30)
    private String regionName;

    @ApiModelProperty(value = "AREA")
    @ExcelProperty(value = "AREA",index = 10)
    @ColumnWidth(value = 30)
    private String areaName;

    @ApiModelProperty(value = "TERRITORY")
    @ExcelProperty(value = "TERRITORY",index = 11)
    @ColumnWidth(value = 30)
    private String territoryName;

    @ApiModelProperty(value = "连锁")
    @ExcelProperty(value = "连锁",index = 12)
    @ColumnWidth(value = 30)
    private String wccsChainCode2022VersionName;

    @ApiModelProperty(value = "档次")
    @ExcelProperty(value = "档次",index = 13)
    @ColumnWidth(value = 30)
    private String wccsGradeCode2022VersionName;

    @ApiModelProperty(value = "销售潜力")
    @ExcelProperty(value = "销售潜力",index = 14)
    @ColumnWidth(value = 30)
    private String wccsSalesPotentialCode2022VersionName;

    @ApiModelProperty(value = "新版渠道")
    @ExcelProperty(value = "新版渠道",index = 15)
    @ColumnWidth(value = 30)
    private String channelInfos;

    @ApiModelProperty(value = "业务员")
    @ExcelProperty(value = "业务员",index = 16)
    @ColumnWidth(value = 30)
    private String salesmanInfo;

    @ApiModelProperty(value = "提交时间")
    @ExcelProperty(value = "提交时间",index = 17)
    @ColumnWidth(value = 30)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    @ApiModelProperty("审批时间")
    @ExcelProperty(value = "审批时间",index = 18)
    @ColumnWidth(value = 30)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditAt;

    @ApiModelProperty(value = "审批备注原因")
    @ExcelProperty(value = "审批备注原因",index = 19)
    @ColumnWidth(value = 30)
    private String auditRemark;

    @ApiModelProperty(value = "售点店主姓名")
    @ExcelProperty(value = "售点店主姓名",index = 20)
    @ColumnWidth(value = 30)
    private String pocOwnerName;

    @ApiModelProperty(value = "售点店主电话")
    @ExcelProperty(value = "售点店主电话",index = 21)
    @ColumnWidth(value = 30)
    private String pocOwnerPhone;

    @ApiModelProperty(value = "一级经销商ID")
    @ExcelProperty(value = "一级经销商ID",index = 22)
    @ColumnWidth(value = 30)
    private String firstWholseId;

    @ApiModelProperty(value = "一级经销商名称")
    @ExcelProperty(value = "一级经销商名称",index = 23)
    @ColumnWidth(value = 30)
    private String firstWholseName;




}
