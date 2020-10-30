package com.springbootbasepackage.service.serviceImpl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONObject;
import com.springbootbasepackage.base.ExcelBodyApply;
import com.springbootbasepackage.base.ExcelListener;
import com.springbootbasepackage.dao.PartnerSourceDAO;
import com.springbootbasepackage.dto.PartnerSourceDTO;
import com.springbootbasepackage.entity.PartnerSourceDO;
import com.springbootbasepackage.service.PartnerSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class PartnerSourceServiceImpl implements PartnerSourceService {

    @Autowired
    private PartnerSourceDAO partnerSourceDAO;

    @Override
    public List<PartnerSourceDTO> testPartnerSource() {
        List<PartnerSourceDTO> list = partnerSourceDAO.testPartnerSource();

        return list;
    }


    //数据导入
    @Override
    public void importExcel(InputStream inputStream, Integer channelType)  {
        ExcelListener listener = new ExcelListener();
        BufferedInputStream buff = new BufferedInputStream(inputStream);

        EasyExcelFactory.readBySax(buff, new Sheet(1, 1, ExcelBodyApply.class), listener);
        List<Object> list = listener.data;
        // 数据去重 -- 参数转化
        List<ExcelBodyApply> resultList = list.stream()
                .map(d -> {
                    ExcelBodyApply excelBodyApply = (ExcelBodyApply) d;
                    return excelBodyApply;
                }).collect(Collectors.toList());
        System.out.println("resultList"+ JSONObject.toJSONString(resultList));
    }

    @Override
    public Integer save(PartnerSourceDTO partnerSourceDTO) {
        PartnerSourceDO partnerSourceDO= new PartnerSourceDO();
        BeanUtils.copyProperties(partnerSourceDTO,partnerSourceDO);
        Integer cnt = partnerSourceDAO.insert(partnerSourceDO);
        return cnt;
    }
}
