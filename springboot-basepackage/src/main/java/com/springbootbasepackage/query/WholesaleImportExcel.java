package com.springbootbasepackage.query;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;


/**
 * 赠酒-售点导入模板
 *
 * @author bangzheng.peng
 */
@Data
public class WholesaleImportExcel {

    @ExcelProperty(value = "经销商类型")
    @ColumnWidth(value = 30)
    private String wholesaleType;

    @ExcelProperty(value = "一级经销商ID/二级经销商ID")
    @ColumnWidth(value = 50)
    private Long wholesaleId;

    @ExcelProperty(value = "经销商名称")
    @ColumnWidth(value = 50)
    private String wholesaleName;

    @ExcelProperty(value = "失败原因")
    @ColumnWidth(value = 50)
    private String fail;
}
