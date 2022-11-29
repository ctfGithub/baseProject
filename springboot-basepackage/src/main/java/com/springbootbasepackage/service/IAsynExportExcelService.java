package com.springbootbasepackage.service;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName: IAsynExportExcelService
 * @Description:
 * @Author: WM
 * @Date: 2021-08-06 20:05
 **/
public interface IAsynExportExcelService {

    /**
     * 分批次异步导出数据
     *
     * @param countDownLatch
     */
    void excuteAsyncTask(Map<String, Object> map, CountDownLatch countDownLatch);

    void excuteAsyncTaskDatabase(Map<String, Object> poll, CountDownLatch cdl);
}
