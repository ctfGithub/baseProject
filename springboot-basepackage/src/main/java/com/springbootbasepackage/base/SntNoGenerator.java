package com.springbootbasepackage.base;

import com.google.common.base.Preconditions;
import com.springbootbasepackage.redis.SedissonManage;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 松鼠billNo生成器
 *
 * @author i_smchen
 * @date 19.12.26
 */
public class SntNoGenerator {

    /**
     * 单号后缀格式
     */
    private static final String NO_FORMAT = "00000000";

    /**
     * 分段id长度
     */
    private Long sectionLength = 100L;

    private SedissonManage sedissonManage;

    private DecimalFormat df = new DecimalFormat(NO_FORMAT);

    private Map<String, Long> maxNoMap = new HashMap<>();

    /**
     * 当前内存中剩余id数
     */
    private Map<String, Long> remainNumMap = new HashMap<>();


    public SntNoGenerator(SedissonManage sedissonManage) {
        this.sedissonManage = sedissonManage;
    }

    public SntNoGenerator(SedissonManage sedissonManage, Long sectionLength) {
        this.sedissonManage = sedissonManage;
        if (sectionLength != null) {
            this.sectionLength = sectionLength;
        }
    }

    /**
     * <功能描述>
     * <功能详细描述>
     * <p>
     *
     * @Description: generateNo
     * @Param: [prefix, appName, dateTime]
     * @return: java.lang.String
     * @see [相关类/方法] （可选）
     * @since [产品/ 模版版本] (可选)
     */
    private String generateNo(String prefix, String appName, String dateTime) {
        StringBuilder redisKey = new StringBuilder(appName)
                .append(":")
                .append(prefix)
                .append(":")
                .append(dateTime);
        Long incrby = sedissonManage.incrby(redisKey.toString(), 1L, 2L, TimeUnit.DAYS);
        StringBuilder no = new StringBuilder();
        no.append(prefix).append(dateTime).append(df.format(incrby));
        return no.toString();
    }

    /**
     * 按调用时间顺序生成自增且连续的单号
     * 每次请求redis生成新的单号，单号生成速度相对较慢
     * 默认时间格式：yyMMdd
     *
     * @param prefix 单号前缀
     * @return
     */
    public String generateNo(String prefix) {
        Preconditions.checkState(StringUtils.isNotBlank(prefix), "no prefix is blank");
        String appName = EnvUtil.getAppName();
        String dateTime = DateUtil.getDateTime(DateUtil.DATE_PATTERN.YYMMDD);
        return generateNo(prefix, appName, dateTime);
    }


    /**
     * 按调用时间顺序生成自增且连续的单号
     * 每次请求redis生成新的单号，单号生成速度相对较慢
     *
     * @param prefix 单号前缀
     * @return
     */
    public String generateNo(String prefix, DateFormatEnum dateFormat) {
        Preconditions.checkState(StringUtils.isNotBlank(prefix), "no prefix is blank");
        String appName = EnvUtil.getAppName();
        String dateTime = DateUtil.getDateTime(dateFormat.getFormatType());
        StringBuilder redisKey = new StringBuilder(appName)
                .append(":")
                .append(prefix)
                .append(":")
                .append(dateTime);
        return generateNo(prefix, appName, dateTime);
    }

    /**
     * 集群环境下按调用顺序生成的单号不连续
     * 同一台机器生成的单号分段自增且连续
     * 一次从redis获取多个单号，缓存为内存中
     * 对单号生成速度要求高，调用此方法
     * 默认时间格式：yyMMdd
     *
     * @param prefix
     * @return
     */
    public String generateDisNo(String prefix) {
        Preconditions.checkState(StringUtils.isNotBlank(prefix), "no prefix is blank");
        String appName = EnvUtil.getAppName();
        String dateTime = DateUtil.getDateTime(DateUtil.DATE_PATTERN.YYMMDD);
        return generateDisNo(prefix, appName, dateTime);
    }

    /**
     * 集群环境下按调用顺序生成的单号不连续
     * 同一台机器生成的单号分段自增且连续
     * 一次从redis获取多个单号，缓存为内存中
     * 对单号生成速度要求高，调用此方法
     *
     * @param prefix
     * @param dateFormat DateFormatEnum
     * @return
     */
    public String generateDisNo(String prefix, DateFormatEnum dateFormat) {
        Preconditions.checkState(StringUtils.isNotBlank(prefix), "no prefix is blank");
        String appName = EnvUtil.getAppName();
        String dateTime = DateUtil.getDateTime(dateFormat.getFormatType());
        return generateDisNo(prefix, appName, dateTime);
    }


    /**
     * <功能描述>
     * <功能详细描述>
     * <p>
     *
     * @Description: generateDisNo
     * @Param: [prefix, appName, dateTime]
     * @return: java.lang.String
     * @see [相关类/方法] （可选）
     * @since [产品/ 模版版本] (可选)
     */
    private String generateDisNo(String prefix, String appName, String dateTime) {
        StringBuilder redisKey = new StringBuilder(appName)
                .append(":")
                .append(prefix)
                .append(":")
                .append(dateTime);
        synchronized (maxNoMap) {
            Long maxNo = maxNoMap.get(redisKey.toString());
            Long remainNum = remainNumMap.get(redisKey.toString());
            if (maxNo == null || maxNo == 0 || remainNum == null || remainNum == 0) {
                maxNo = sedissonManage.incrby(redisKey.toString(), sectionLength, 2L, TimeUnit.DAYS);
                maxNoMap.put(redisKey.toString(), maxNo);
                remainNum = sectionLength;
            }
            remainNum--;
            remainNumMap.put(redisKey.toString(), remainNum);
            StringBuilder no = new StringBuilder();
            no.append(prefix).append(dateTime).append(df.format(maxNo - remainNum));
            return no.toString();

        }

    }


}
