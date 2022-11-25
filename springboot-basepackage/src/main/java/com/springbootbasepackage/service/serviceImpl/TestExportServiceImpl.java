package com.springbootbasepackage.service.serviceImpl;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.springbootbasepackage.base.DateUtil;
import com.springbootbasepackage.dto.BccAuditDetailExcelDTO;
import com.springbootbasepackage.dto.BccAuditExcelDTO;
import com.springbootbasepackage.service.TestExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TestExportServiceImpl implements TestExportService {

    @Override
    public void exportExcel(HttpServletResponse response) {
        List<BccAuditExcelDTO> excelVOS = new ArrayList<>();//列表数据
        List<BccAuditDetailExcelDTO> excelDeatils = new ArrayList<>();//列表数据 -包含审核 轨迹

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        try {
            String fileName = URLEncoder.encode("bcc列表", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //新建ExcelWriter
            com.alibaba.excel.ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            //获取sheet0对象
            WriteSheet mainSheet = EasyExcel.writerSheet(0, "bcc审核列表数据").head(BccAuditExcelDTO.class).build();
            //获取模型信息,向sheet0写入数据
            excelWriter.write(excelVOS, mainSheet);
            //获取sheet1对象
            WriteSheet detailSheet = EasyExcel.writerSheet(1, "bcc审核列表数据明细").head(BccAuditDetailExcelDTO.class).build();
            excelWriter.write(excelDeatils, detailSheet);
            //关闭流
            excelWriter.finish();
        } catch (IOException e) {
            log.error("导出异常{}", e.getMessage());
        }
    }

    @Override
    public void exportExcelBySingleExcoter() {
        String fileName = "导出" + DateUtil.localDateTimeConvertStr(LocalDateTime.now()) + ".xlsx";
        String templateFileName = System.getProperty("user.dir")+ File.separator+fileName;

        try {
            FileOutputStream output = new FileOutputStream(templateFileName);
            ExcelWriter excelWriter = EasyExcel.write(output).build();

            //分页查询 避免 内存泄露
            //eg：例如 数据总数 是10001，分页的查询 是每次 100 ，可以动态 配置
            Integer couponBeesTaskIPageCount = 1001;
            Integer exportNum =100;

            Integer pageNoCount = (couponBeesTaskIPageCount / exportNum) + (couponBeesTaskIPageCount % exportNum == 0 ? 0 : 1);
            for (int pageNo = 0; pageNo < pageNoCount; pageNo++) {
                //执行sql  分页查询 current  = i ;pageSize = exportNum
                List<BccAuditExcelDTO> usedCouponProperties= new ArrayList<>();
                List<BccAuditDetailExcelDTO> couponRecordSummaryProperties= new ArrayList<>();
                WriteSheet writeSheet1 = EasyExcel.writerSheet((2 * pageNo), "单数sheet"+pageNo)
                        .head(BccAuditExcelDTO.class)
                        .build();
                WriteSheet writeSheet2 = EasyExcel.writerSheet((2 * pageNo+1), "双数sheet"+pageNo)
                        .head(BccAuditDetailExcelDTO.class)
                        .build();
                excelWriter.write(usedCouponProperties, writeSheet1);
                excelWriter.write(couponRecordSummaryProperties, writeSheet2);

            }

            // 关闭流
            excelWriter.finish();
            output.flush();

            //封装MultipartFile对象
            File file = new File(templateFileName);
            MultipartFile multipartFile = new CommonsMultipartFile(createFileItem(file.getPath(),file.getName()));

            //上传文件服务器 ，生成文件链接


        }catch (Exception e){
            //异常记录 一下
            log.error("导出报错{}", JSONUtil.toJsonStr(e));
        }finally {
            if(StringUtils.isNotBlank(templateFileName)){
                try {
                    Files.deleteIfExists(Paths.get(templateFileName));
                } catch (IOException e) {
                    log.error("deleteIfExists occur exception:{}", e);
                }
            }
        }


    }


    public static FileItem createFileItem(String filePath, String fileName){
        String fieldName = "file";
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(fieldName, "text/plain", false,fileName);
        File newfile = new File(filePath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try (FileInputStream fis = new FileInputStream(newfile);
             OutputStream os = item.getOutputStream()) {
            while ((bytesRead = fis.read(buffer, 0, 8192))!= -1)
            {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public void exportExcelByManyExcoter() {

    }
}
