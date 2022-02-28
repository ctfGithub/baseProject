package com.springbootbasepackage.controller;

import com.springbootbasepackage.base.SntResult;
import com.springbootbasepackage.service.PartnerSourceService;
import io.shardingsphere.transaction.annotation.ShardingTransactionType;
import io.shardingsphere.transaction.api.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/testImport")
public class TestImport {

    @Autowired
    private PartnerSourceService partnerSourceService;

    @PostMapping("/importExcel")
    @ShardingTransactionType(TransactionType.XA)
    public SntResult<Integer> importExcel(@RequestParam("file") MultipartFile file, Integer channelType) {
        //校验文件必填

        //
        try {
            partnerSourceService.importExcel(file.getInputStream(), channelType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SntResult.ok();
    }
}
