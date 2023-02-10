package com.springbootbasepackage.util;


import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: Holiday
 * @Author: lxh
 * @Description: 节假日
 * @Date: 2022/3/19 17:29
 */
public class HolidayUtils {
    /**
     * java获取国家法定节假日和周末
     * @param year /
     * @param month /
     * @return /
     */
    public static Set<String> JJR(int year, int month) {
        //获取所有的周末
        Set<String> monthWekDay = getMonthWekDay(year, month);
        //http://timor.tech/api/holiday api文档地址
        Map jjr = getJjr(year, month + 1);
        Integer code = (Integer) jjr.get("code");
        if (code != 0) {
            return monthWekDay;
        }
        Map<String, Map<String, Object>> holiday = (Map<String, Map<String, Object>>) jjr.get("holiday");
        Set<String> strings = holiday.keySet();
        for (String str : strings) {
            Map<String, Object> stringObjectMap = holiday.get(str);
            Integer wage = (Integer) stringObjectMap.get("wage");
            String date = (String) stringObjectMap.get("date");
            //筛选掉补班
            if (wage.equals(1)) {
                monthWekDay.remove(date);
            } else {
                monthWekDay.add(date);
            }
        }
        return monthWekDay;
    }

    /**
     * 获取节假日不含周末
     * @param year /
     * @param month /
     * @return /
     */
    private static Map getJjr(int year, int month) {
        String url = "http://timor.tech/api/holiday/year/";
        OkHttpClient client = new OkHttpClient();
        Response response;
        //解密数据
        String rsa = null;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            response = client.newCall(request).execute();
            rsa = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(rsa, Map.class);
    }

    /**
     * 获取周末  月从0开始
     * @param year /
     * @param month /
     * @return /
     */
    public static Set<String> getMonthWekDay(int year, int month) {
        Set<String> dateList= new LinkedHashSet<>();
        SimpleDateFormat simdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar(year, month, 1);
        int i = 1;
        while (calendar.get(Calendar.YEAR) < year + 1) {
            calendar.set(Calendar.WEEK_OF_YEAR, i++);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            if (calendar.get(Calendar.YEAR) == year) {
                dateList.add(simdf.format(calendar.getTime()));
            }
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            if (calendar.get(Calendar.YEAR) == year) {
                dateList.add(simdf.format(calendar.getTime()));
            }
        }
        return dateList;
    }

    public static void main(String[] args) {
        System.out.println(JJR(2022, 4));

    }
}