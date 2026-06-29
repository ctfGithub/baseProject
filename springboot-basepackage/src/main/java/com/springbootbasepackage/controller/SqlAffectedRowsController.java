package com.springbootbasepackage.controller;

import com.springbootbasepackage.base.SntResult;
import com.springbootbasepackage.dto.SqlAffectedResult;
import com.springbootbasepackage.dto.SqlExecuteDTO;
import com.springbootbasepackage.service.SqlAffectedRowsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/sql")
@RestController
@Api(tags = "SQL影响行数检测")
public class SqlAffectedRowsController {

    @Autowired
    private SqlAffectedRowsService sqlAffectedRowsService;

    @PostMapping("/check")
    @ApiOperation("检测SQL影响行数（多条用;分隔，不实际执行，报错显示错误语法）")
    public SntResult<List<SqlAffectedResult>> check(@RequestBody @Valid SqlExecuteDTO dto) {
        List<SqlAffectedResult> results = sqlAffectedRowsService.check(dto);
        return SntResult.ok(results);
    }
}
