package com.springbootbasepackage.service.serviceImpl;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.springbootbasepackage.base.DateUtil;
import com.springbootbasepackage.dto.BccAuditDetailExcelDTO;
import com.springbootbasepackage.dto.BccAuditExcelDTO;
import com.springbootbasepackage.service.IAsynExportExcelService;
import com.springbootbasepackage.service.TestExportService;
import com.springbootbasepackage.util.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import static com.springbootbasepackage.service.serviceImpl.IAsynExportExcelServiceImpl.DATA_TOTAL_COUNT;

@Service
@Slf4j
public class TestExportServiceImpl implements TestExportService {

    // 定义导出的excel文件保存的路径
    private String filePath = "C:\\Users\\Administrator\\Desktop\\execl\\";
    @Resource
    private IAsynExportExcelService asynExportExcelService;
    /**
     * 每批次处理的数据量
     */
    private static final int LIMIT = 40000;
    // Queue是java自己的队列，是同步安全的
    public static Queue<Map<String, Object>> queue;

    static {
        // 一个基于链接节点的无界线程安全的队列
        queue = new ConcurrentLinkedQueue<>();
    }

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
    public void exportExcelByManyExcoter(HttpServletResponse response) {
        long start = System.currentTimeMillis();
        initQueue();
        //异步转同步，等待所有线程都执行完毕返回 主线程才会结束
        try {

            CountDownLatch cdl = new CountDownLatch(queue.size());
            log.info("需要的线程数:{}",queue.size());
            while (queue.size() > 0) {
                asynExportExcelService.excuteAsyncTaskDatabase(queue.poll(), cdl);
            }
            cdl.await();//能够阻塞线程 直到调用N次 countDown() 方法才释放线程
            log.info("excel导出完成·······················");
            //关闭流 等操作
            //压缩文件
            File zipFile = new File(filePath.substring(0, filePath.length() - 1) + ".zip");
            FileOutputStream fos1 = new FileOutputStream(zipFile);
            //压缩文件目录
            ZipUtils.toZip(filePath, fos1, true);
            //发送zip包
            ZipUtils.sendZip(response, zipFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        log.info("任务执行完毕共消耗：  " + (end - start) + "ms");

    }

    /**
     * 初始化队列
     */
    public void initQueue() {
        long dataTotalCount = DATA_TOTAL_COUNT;// 数据的总数
        int listCount = (int) dataTotalCount;
        // 计算出多少页，即循环次数
        int count = listCount / LIMIT + (listCount % LIMIT > 0 ? 1 : 0);
        for (int i = 1; i <= count; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("page", i);
            map.put("limit", LIMIT);
            map.put("path", filePath);
            //添加元素
            queue.offer(map);
        }
    }


}
