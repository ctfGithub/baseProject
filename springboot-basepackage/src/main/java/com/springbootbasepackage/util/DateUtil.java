package com.springbootbasepackage.util;

import com.alibaba.excel.util.DateUtils;
import com.springbootbasepackage.enums.DateUnitEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
@Slf4j
public class DateUtil {

    /**
     * 获取当前时间是本月第几天
     *
     * @return
     */
    public static Integer getCurrentMonthDays() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 日期转换
     *
     * @param yyyyMMdd
     * @return
     */
    public static Date parseYyyyMMdd(String yyyyMMdd, boolean is235959) {
        if (StringUtils.isEmpty(yyyyMMdd)) {
            return null;
        }
        String yyyyMMddHHmmss = yyyyMMdd + "" + (is235959 ? "235959" : "000000");
        try {
            return DateUtils.parseDate(yyyyMMddHHmmss, DateUtil.DATE_PATTERN.YYYYMMDDHHMMSS);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 日期转换
     * 格式 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static Date parseDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        try {
            return DateUtils.parseDate(date, DATE_PATTERN.YYYY_MM_DD_HH_MM_SS);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 在前的日期做为起始时间，在后的做为结束时间
     *
     * @param begin 起始时间
     * @param end   结束时间
     * @param isAbs 日期间隔是否只保留绝对值正数
     */
    public static Long between(Date begin, Date end, DateUnitEnum dateUnit, boolean isAbs) {
        Date temp = null;
        if (isAbs && begin.after(end)) {
            // 间隔只为正数的情况下，如果开始日期晚于结束日期，置换之
            temp = begin;
            begin = end;
            end = temp;
        }
        long diff = end.getTime() - begin.getTime();
        return diff / dateUnit.getMillis();
    }

    public static int currYear() {
        Calendar cale = Calendar.getInstance();
        int year = cale.get(Calendar.YEAR);
        return year;
    }

    public static int getYear(Date date) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        int year = cale.get(Calendar.YEAR);
        return year;
    }

    public static int currMonth() {
        Calendar cale = Calendar.getInstance();
        int month = cale.get(Calendar.MONTH) + 1;
        return month;
    }

    public static int currHour() {
        Calendar cale = Calendar.getInstance();
        int hour = cale.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    public static int currDayOfMonth() {
        Calendar cale = Calendar.getInstance();
        int dayOfMonth = cale.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth;
    }

    public static Date addDay(Date d, int i) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(d);
        cale.add(Calendar.DAY_OF_MONTH, i);
        return cale.getTime();
    }

    public static Date addMonth(Date d, int i) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(d);
        cale.add(Calendar.MONTH, i);
        return cale.getTime();
    }


    public static Date addMonthPlanDate(Date d, int i) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(d);
        cale.add(Calendar.MONTH, i);
        cale.set(cale.get(Calendar.YEAR), cale.get(Calendar.MONTH), 15, 23, 59, 59);
        cale.set(Calendar.MILLISECOND, 0);
        return cale.getTime();
    }

    public static Date addYear(Date d, int i) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(d);
        cale.add(Calendar.YEAR, i);
        return cale.getTime();
    }

    /**
     * 返回当前日期 yyyyMMdd 00:00:00
     *
     * @return
     */
    public static Date getCurrentDate(boolean is235959) {
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        if (is235959) {
            cale.set(Calendar.HOUR_OF_DAY, 23);
            cale.set(Calendar.MINUTE, 59);
            cale.set(Calendar.SECOND, 59);
        }
        cale.set(Calendar.MILLISECOND, 0);
        return cale.getTime();
    }

    /**
     * 返回当前日期 yyyyMMdd 00:00:00
     *
     * @return
     */
    public static Date todayDay() {
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        cale.set(Calendar.MILLISECOND, 0);
        return cale.getTime();
    }


    /**
     * 开始时间计算贷款到期时间
     *
     * @param startDate
     * @return
     */
    public static Date planExpireDate(Date startDate, int addMonth) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar cale = Calendar.getInstance();
        cale.setTime(addMonth(startDate, addMonth));
        cale.set(cale.get(Calendar.YEAR), cale.get(Calendar.MONTH), 15, 23, 59, 59);
        cale.set(Calendar.MILLISECOND, 0);
        return cale.getTime();
    }

    /**
     * 获取日期相差天数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static long dayDiff(Date startTime, Date endTime) {
        LocalDate start = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long diff = ChronoUnit.DAYS.between(start, end);
        return Math.abs(diff);
    }


    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static Date getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        Date today = calendar.getTime();

        return today;
    }

    /**
     * 比较两个日期  按照日比较
     *
     * @param nowTime
     * @param startTime
     * @param
     * @return 0 : 相等  1：大于  -1:小于
     */
    public static int compareDate(Date nowTime, Date startTime) {
        String now = DateUtil.getTimeStr(nowTime, "yyyyMMdd");
        String start = DateUtil.getTimeStr(startTime, "yyyyMMdd");
        if (now.compareTo(start) == 0) {
            return 0;
        } else if (now.compareTo(start) > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public static String getTimeStr(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format == null ? DATE_PATTERN.YYYYMMDD : format);
        return formatter.format(date);
    }

    public static Date getDate(String dateStr, String srcFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(srcFormat == null ? DATE_PATTERN.YYYYMMDD : srcFormat);
        Date parse = null;
        try {
            parse = formatter.parse(dateStr);
        } catch (ParseException e) {

        }
        return parse;
    }

    public static final String format(Object date) {

        return format(date, "yyyy-MM-dd");
    }

    public static final String format(Object date, String pattern) {
        if (date == null) {
            return null;
        } else {
            return pattern == null ? format(date) : (new SimpleDateFormat(pattern)).format(date);
        }
    }


    public interface DATE_PATTERN {
        String HHMMSS = "HHmmss";
        String HH_MM_SS = "HH:mm:ss";
        String YYYY = "yyyy";
        String YYYYMM = "yyyyMM";
        String YYYY_MM = "yyyy-MM";
        String YYYYMMDD = "yyyyMMdd";
        String YYYY_MM_DD = "yyyy-MM-dd";
        String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
        String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
        String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
        String DYA_START = " 00:00:00";
        String DAY_END = " 23:59:59";
    }

    /**
     * Date转字符串
     *
     * @param date Date实例
     * @return 返回Date实例对应的时间字符串
     */
    public static String dateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String dateToStringSimple(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * 时间戳转为为时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public static String unixTime(Long unix) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
        Date d = new Date(unix);
        String t = df.format(d);
        return t;
    }

    /**
     * 每月第一天
     *
     * @return
     */
    public static Date getMonthFirstDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar cale = Calendar.getInstance();
        // 获取前月的第一天
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        String firstDay = format.format(cale.getTime());
        Date monthFirstDay = DateUtil.parseYyyyMMdd(firstDay, false);
        return monthFirstDay;
    }

    /**
     * 每月最后一天
     *
     * @return
     */
    public static Date getMonthLastDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar cale = Calendar.getInstance();
        // 获取前月的第一天
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        String lastDay = format.format(cale.getTime());
        Date monthLastDay = DateUtil.parseYyyyMMdd(lastDay, true);
        return monthLastDay;
    }

    /**
     * 获取零点零分零秒 day=0 表示是今天0点，-1 是昨天的零点
     */
    public static Date getZeroClock(int day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        Date time = cal.getTime();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date zero = cal.getTime();
        return zero;
    }


    public static Date getDay(String day) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
        Date date = null;
        try {
            date = simpleDateFormat.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * date  转 年月日   比如  2020-12-08 转成2020年12月8日
     *
     * @param day
     * @return
     */
    public static String day(Date day) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 时间格式转换  20200908 转 T ue Sep 08 00:00:00 CST 2020
     *
     * @param ss
     * @return
     */
    public static Date getDateTime(String ss) {
        String s = ss.substring(0, 4) + "-" + ss.substring(4, 6) + "-" + ss.substring(6, 8);
        Date date = DateUtil.getDate(s, DateUtil.DATE_PATTERN.YYYY_MM_DD);
        return date;
    }

    /**
     * date 减去一个天数
     */
    public static Date getDateSubtractTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        Date time = calendar.getTime();
        return time;

    }


    /**
     * 获取当前是几点钟
     */
    public static int timeClock() {
        LocalTime time = LocalTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String format = time.format(formatter);
        String substring = format.substring(0, 2);
        int i = Integer.parseInt(substring);
//        System.out.println(i);
        return i;

    }

    /**
     * 一周的第一天和最后一天的日期 （星期一是第一天，日 是最后一天）
     */
    public void dateInfo(){
        // 获取当前日期
        Date currentDate = new Date();

        // 创建一个Calendar实例，并设置当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // 设置一周的第一天为星期一
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        // 获取本周的第一天
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Date firstDayOfWeek = calendar.getTime();

        // 获取本周的最后一天
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
        Date lastDayOfWeek = calendar.getTime();

        // 输出结果
        System.out.println("本周的第一天：" + firstDayOfWeek);
        System.out.println("本周的最后一天：" + lastDayOfWeek);
    }


    public static void main(String[] args) {
        int i = DateUtil.timeClock();
        System.out.println(i);

    }




}
