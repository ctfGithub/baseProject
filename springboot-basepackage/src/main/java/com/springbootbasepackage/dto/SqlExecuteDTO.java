package com.springbootbasepackage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("SQL检测请求")
public class SqlExecuteDTO {

    @ApiModelProperty(value = "数据库名（可选，不传则用默认库）", example = "baiwei")
    @NotBlank(message = "数据库名不能为空")
    private String databaseName;

    @ApiModelProperty(value = "SQL语句，多条用 ; 分隔", required = true,
            example = "INSERT INTO cpe_activity (mp_id, activity_code) VALUES ('budweiser', 'IQMS001');\nUPDATE user SET status = 1 WHERE id < 100")
    private String sql;
}
