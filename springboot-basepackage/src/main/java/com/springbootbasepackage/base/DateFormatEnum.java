package com.springbootbasepackage.base;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 * <p>
 *
 * @author Created by szyue on 2020/02/26 .
 * @see [相关类/方法]（可选）
 * @since [产品 /模块版本] （可选）
 */
public enum DateFormatEnum {

    /**
     * HHmmss
     */
    HHMMSS("HHmmss"),
    /**
     * HH:mm:ss
     */
    HH_MM_SS("HH:mm:ss"),
    /**
     * yyMMdd
     */
    YYMMDD("yyMMdd"),
    /**
     * yyyyMMdd
     */
    YYYYMMDD("yyyyMMdd"),
    /**
     * yyyy-MM-dd
     */
    YYYY_MM_DD("yyyy-MM-dd"),
    /**
     * yyyyMMddHHmmss
     */
    YYYYMMDDHHMMSS("yyyyMMddHHmmss"),
    /**
     * yyyyMMddHHmmssSSS
     */
    YYYYMMDDHHMMSSSSS("yyyyMMddHHmmssSSS"),
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    YYYY_MM_DD_HH_MM_SS_SSS("yyyy-MM-dd HH:mm:ss.SSS"),

    ;
    private String formatType;

    DateFormatEnum(String formatType) {
        this.formatType = formatType;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }
}
