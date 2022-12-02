package com.springbootbasepackage.controller;

import com.springbootbasepackage.dto.FundTransactApplyDTO;
import com.springbootbasepackage.query.FundTransactApplyQuery;
import com.springbootbasepackage.base.ExcelExport;
import com.springbootbasepackage.base.Export;
import com.springbootbasepackage.redis.SedissonManage;
import com.springbootbasepackage.service.TestExportService;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/testExport")
public class TestExportController {

    @Resource
    private TestExportService testExportService;

    //@Resource
    private SedissonManage sedissonManage;

    /**
     * 第一种方法导出 （实时响应 前端）
     * @param query
     * @param response
     * @return
     * @throws Exception
     */
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


    /**
     * 导出响应到 前端 -第二种写法
     * @param response
     */
    @PostMapping("/query/exportExcel")
    public void exportExcel( HttpServletResponse response) {
        testExportService.exportExcel(response);
    }


    /**
     * 导出异步 导出 （单线程）-文件上传到 文件服务器 excel 文件
     */

    @PostMapping("/query/exportExcelBySingleExcoter")
    public void exportExcelBySingleExcoter() {
        boolean ceshi = sedissonManage.tryLock("ceshi", TimeUnit.SECONDS, 50, 5);
        if(ceshi){
            CompletableFuture.runAsync(()->{
                testExportService.exportExcelBySingleExcoter();
            });
        }
    }


    /**
     * 导出异步 导出 （单线程）-文件上传到 文件服务器 zip 文件
     */

    @PostMapping("/query/exportExcelBySingleExcoterZIP")
    public void exportExcelBySingleExcoterZIP() {
        boolean ceshi = sedissonManage.tryLock("ceshi", TimeUnit.SECONDS, 50, 5);
        if(ceshi){
            CompletableFuture.runAsync(()->{
                testExportService.exportExcelBySingleExcoterZIP();
            });
        }
    }


    /**
     * 导出异步 导出 （多线程）-文件上传到 文件服务器
     */

    @PostMapping("/query/exportExcelByManyExcoter")
    public void exportExcelByManyExcoter(HttpServletResponse response) {
        testExportService.exportExcelByManyExcoter(response);
    }


}
