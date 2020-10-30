package com.springbootbasepackage.base;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExcelBodyApply extends BaseRowModel {

    /**
     * 伙伴id
     */
    @ExcelProperty(index = 0)
    private String partnerId;

    /**
     * 伙伴名称
     */
    @ExcelProperty(index = 1)
    private String partnerName;

    /**
     * 伙伴得分
     */
    @ExcelProperty(index = 2)
    private Integer partnerSource  ;



}
