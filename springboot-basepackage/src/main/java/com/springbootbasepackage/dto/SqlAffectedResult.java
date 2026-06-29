package com.springbootbasepackage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("单条SQL检测结果")
public class SqlAffectedResult {

    @ApiModelProperty("原始SQL")
    private String sql;

    @ApiModelProperty("sql影响行数（语法错误时显示：错误语法）")
    private String affectedRows;

    /**
     * 成功：返回影响行数
     */
    public static SqlAffectedResult ok(String sql, int rows) {
        return new SqlAffectedResult(sql, String.valueOf(rows));
    }

    /**
     * 失败：返回"错误语法"
     */
    public static SqlAffectedResult fail(String sql) {
        return new SqlAffectedResult(sql, "错误语法");
    }
}
