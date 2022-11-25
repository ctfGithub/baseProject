package com.springbootbasepackage.service;


import javax.servlet.http.HttpServletResponse;

public interface TestExportService {

    void exportExcel(HttpServletResponse response);

    void exportExcelBySingleExcoter();

    void exportExcelByManyExcoter();
}
