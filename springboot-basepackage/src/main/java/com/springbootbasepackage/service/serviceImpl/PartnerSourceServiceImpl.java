package com.springbootbasepackage.service.serviceImpl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONObject;
import com.springbootbasepackage.base.ExcelBodyApply;
import com.springbootbasepackage.base.ExcelListener;
import com.springbootbasepackage.base.SntException;
import com.springbootbasepackage.dao.PartnerSourceDAO;
import com.springbootbasepackage.dto.PartnerSourceDTO;
import com.springbootbasepackage.dto.PartnerSourceDeleteDTO;
import com.springbootbasepackage.entity.PartnerSourceDO;
import com.springbootbasepackage.service.PartnerSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Slf4j
public class PartnerSourceServiceImpl implements PartnerSourceService {

    private static final String ZB_INCREMENT = "bees:biz-zb-increment-id:";
    private static final DecimalFormat ID_FORMAT = new DecimalFormat("000000");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");



    @Autowired
    private PartnerSourceDAO partnerSourceDAO;

    @Resource
    private RedisTemplate redisTemplate;

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

    @Override
    public String testRedis() {
        // 业务code
        String bizCode = "ZB";
        // 年月日
        String yyyyMMdd = FORMATTER.format(LocalDateTime.now());
        // 当日订单号:key
        String key = ZB_INCREMENT + bizCode  + yyyyMMdd;
        // 当日订单号
        String id = ID_FORMAT.format(redisTemplate.opsForValue().increment(key));
        System.out.println(bizCode  + yyyyMMdd + id);
        return bizCode  + yyyyMMdd + id;
    }

    @Override
    public Integer update(PartnerSourceDTO partnerSourceDTO) {
        if(Objects.isNull(partnerSourceDTO.getId())){
           throw new SntException("更新操作-主键id不能为空") ;
        }
        PartnerSourceDO PartnerSourcedO= new PartnerSourceDO();
        BeanUtils.copyProperties(partnerSourceDTO,PartnerSourcedO);
        return partnerSourceDAO.update(PartnerSourcedO);
    }

    @Override
    public Integer delete(PartnerSourceDeleteDTO partnerSourceDTO) {
        Assert.notNull(partnerSourceDTO.getId(),"删除操作-主键id不能为空");
        return partnerSourceDAO.delete(partnerSourceDTO.getId());
    }
}
