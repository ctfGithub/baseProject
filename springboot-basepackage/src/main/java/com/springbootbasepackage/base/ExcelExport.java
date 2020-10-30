package com.springbootbasepackage.base;


import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.springbootbasepackage.util.DateUtil;
import com.springbootbasepackage.util.JsonUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelExport implements Export {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExport.class);
    private static final String EXCEL_SUFFIX = ".xlsx";

    public ExcelExport() {
    }

    public <T> byte[] export(List<T> data, Class<T> dataType) throws Exception {
        Map<String, String> headerMap = new HashMap();
        List<String> columns = this.columns(dataType);
        columns.forEach((c) -> {
            String var10000 = (String) headerMap.put(c, c);
        });
        return this.export(data, dataType, headerMap);
    }

    public <T> byte[] export(List<T> data, Class<T> dataType, Map<String, String> headerMap) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<String> headerKeys = new ArrayList(headerMap.keySet());
        Row header = sheet.createRow(0);
        IntStream.range(0, headerKeys.size()).forEach((idx) -> {
            header.createCell(idx).setCellValue((String) headerMap.get(headerKeys.get(idx)));
        });
        IntStream.range(0, data.size()).forEach((idx) -> {
            Row row = sheet.createRow(idx + 1);
            T d = data.get(idx);
            IntStream.range(0, headerKeys.size()).forEach((cidx) -> {
                try {
                    Method getter = dataType.getMethod(this.getterMethodNameByFieldName((String) headerKeys.get(cidx)));
                    Object v = getter.invoke(d);
                    Cell cell = row.createCell(cidx);
                    if (null != v) {
                        if (v instanceof String) {
                            cell.setCellValue((String) v);
                        } else if (v instanceof Number) {
                            cell.setCellValue(((Number) v).doubleValue());
                        } else if (v instanceof BigDecimal) {
                            cell.setCellValue(((BigDecimal) v).doubleValue());
                        } else if (v instanceof Date) {
                            cell.setCellValue(DateUtil.format(v));
                        } else {
                            cell.setCellValue(JsonUtil.toJson(v));
                        }
                    }
                } catch (Exception var10) {
                    LOGGER.error("create cell failed: rowNum:{}, column:{}", new Object[]{idx, headerKeys.get(cidx), var10});
                }

            });
        });
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            workbook.write(out);
        } finally {
            out.close();
        }

        return out.toByteArray();
    }

    public byte[] export(List<List<Object>> data, List<Class> dataType, List<Map<String, String>> headerMap, List<String> sheetNames) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        int i = 0;
        Iterator var7 = data.iterator();

        while (var7.hasNext()) {
            List<Object> objects = (List) var7.next();
            Sheet sheet = workbook.createSheet();
            String sheetName = (String) sheetNames.get(i);
            workbook.setSheetName(i, sheetName);
            Map<String, String> child_headerMap = (Map) headerMap.get(i);
            Class child_dataType = (Class) dataType.get(i);
            List<String> headerKeys = new ArrayList(child_headerMap.keySet());
            ++i;
            Row header = sheet.createRow(0);
            IntStream.range(0, headerKeys.size()).forEach((idx) -> {
                header.createCell(idx).setCellValue((String) child_headerMap.get(headerKeys.get(idx)));
            });
            IntStream.range(0, objects.size()).forEach((idx) -> {
                Row row = sheet.createRow(idx + 1);
                Object d = objects.get(idx);
                IntStream.range(0, headerKeys.size()).forEach((cidx) -> {
                    try {
                        Method getter = child_dataType.getMethod(this.getterMethodNameByFieldName((String) headerKeys.get(cidx)));
                        Object v = getter.invoke(d);
                        Cell cell = row.createCell(cidx);
                        if (null != v) {
                            if (v instanceof String) {
                                cell.setCellValue((String) v);
                            } else if (v instanceof Number) {
                                cell.setCellValue(((Number) v).doubleValue());
                            } else if (v instanceof BigDecimal) {
                                cell.setCellValue(((BigDecimal) v).doubleValue());
                            } else if (v instanceof Date) {
                                cell.setCellValue(DateUtil.format(v));
                            } else {
                                cell.setCellValue(JsonUtil.toJson(v));
                            }
                        }
                    } catch (Exception var10) {
                        LOGGER.error("create cell failed: rowNum:{}, column:{}", new Object[]{idx, headerKeys.get(cidx), var10});
                    }

                });
            });
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            workbook.write(out);
        } finally {
            out.close();
        }

        return out.toByteArray();
    }

    private List<String> columns(Class dataType) {
        return (List) Stream.concat(Stream.of(dataType.getDeclaredFields()), Stream.of(dataType.getSuperclass().getDeclaredFields())).filter((field) -> {
            return !Modifier.isStatic(field.getModifiers());
        }).map((field) -> {
            return field.getName();
        }).collect(Collectors.toList());
    }

    private String getterMethodNameByFieldName(String fieldName) {
        String firstChar = String.valueOf(fieldName.charAt(0));
        return "get" + fieldName.replaceFirst(firstChar, firstChar.toUpperCase());
    }

}
