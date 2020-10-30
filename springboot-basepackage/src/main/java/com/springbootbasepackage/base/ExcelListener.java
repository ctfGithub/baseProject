package com.springbootbasepackage.base;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析器 -- 直接解析excel文件
 */
public class ExcelListener extends AnalysisEventListener {

    public List<Object> data = new ArrayList<Object>();

    @Override
    public void invoke( Object o,  AnalysisContext analysisContext) {
        data.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
