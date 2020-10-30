package com.springbootbasepackage.controller;

import com.springbootbasepackage.dto.FundTransactApplyDTO;
import com.springbootbasepackage.query.FundTransactApplyQuery;
import com.springbootbasepackage.base.ExcelExport;
import com.springbootbasepackage.base.Export;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testExport")
public class TestExport {

    @GetMapping("/queryTiXianExport")
    public HttpEntity<byte[]> queryTiXianExport(FundTransactApplyQuery query, HttpServletResponse response) throws Exception {
        List<FundTransactApplyDTO> list = new ArrayList<>();

        FundTransactApplyDTO dto = new FundTransactApplyDTO();
        dto.setReceiveAccount("ceshi");
        dto.setBankFlowNo("12323");
        list.add(dto);

        Map<String, String> headMap = new LinkedHashMap<>();
        headMap.put("receiveAccountName", "收款银行账户名");
        headMap.put("receiveBankName", "收款开户行");
        headMap.put("receiveAccount", "收款银行账号");
        headMap.put("bankFlowNo", "银行流水号");

        Export export = new ExcelExport();
        byte[] bytes = export.export(list, FundTransactApplyDTO.class, headMap);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String("口袋-提现流水导出".getBytes("GB2312"), "ISO8859-1") + System.currentTimeMillis() + ".xlsx");
        return new HttpEntity<>(bytes);
    }

}
