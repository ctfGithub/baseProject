package com.springbootbasepackage.service.serviceImpl;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootbasepackage.base.ExcelBodyApply;
import com.springbootbasepackage.base.ExcelListener;
import com.springbootbasepackage.base.SntException;
import com.springbootbasepackage.dao.PartnerSourceDAO;
import com.springbootbasepackage.dto.ActivityImportWholesaleDto;
import com.springbootbasepackage.dto.PartnerSourceDTO;
import com.springbootbasepackage.dto.PartnerSourceDeleteDTO;
import com.springbootbasepackage.entity.PartnerSourceDO;
import com.springbootbasepackage.exception.BusinessException;
import com.springbootbasepackage.query.WholesaleImportExcel;
import com.springbootbasepackage.service.PartnerSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.IOException;
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


    @Resource
    private ObjectMapper objectMapper;


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
    public static final String xlsx = "xlsx";
    public static final  String xls = "xls";

    @Override
    public ActivityImportWholesaleDto wholesalerList(MultipartFile file) {
        ActivityImportWholesaleDto activityImportWholesaleDto= new ActivityImportWholesaleDto();
        try {
            if (file != null && file.getSize() > 0) {
                String fileName = file.getOriginalFilename();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                if (!xlsx.equals(suffix) && !xls.equals(suffix)) {
                    throw new BusinessException("上传文件格式不正确");
                }
                //解析Excel 文件的内容
                ExcelListener listener = new ExcelListener();
                //重第三行开始读取数据（第一个sheet页，读取数据，第一行可能是多列 的一个说明，第二行表头对应实体类ExcelProperty值，第三行是数据）
                List<Object> list = EasyExcel.read(file.getInputStream(), WholesaleImportExcel.class, listener).headRowNumber(2).doReadAllSync();

                //重第二行开始读取数据（第一个sheet页，正常读取数据，第一行表头对应实体类ExcelProperty值，第二行是数据）
                //EasyExcel.read(file.getInputStream(), com.abi.bees.promotion.dto.activityReq.WholesaleImportExcel.class, listener).sheet().doRead();

                log.info("excel文件数据：{}", JSONUtil.toJsonStr(list));

                List<WholesaleImportExcel> resultList = list.stream()
                        .map(d -> {
                            WholesaleImportExcel wholesaleExcelTemplate = (WholesaleImportExcel) d;
                            return wholesaleExcelTemplate;
                        }).collect(Collectors.toList());

                log.info("excel数据：{}",JSONUtil.toJsonStr(resultList));

            } else {
                throw new BusinessException( "上传文件不存在");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return activityImportWholesaleDto;
    }
}
