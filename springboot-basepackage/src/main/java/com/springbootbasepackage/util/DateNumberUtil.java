package com.springbootbasepackage.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Date;


@Slf4j
@Component
public class DateNumberUtil {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedissonClient redisson;

    /**
     * 按照年月日生成序列号编号
     * @return
     */
    public String YearMonthDayNumber(){

        String dateString = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        String key = "Base-Service-DateTime1:" + dateString;
        Jedis jedis = new Jedis("127.0.0.1");
        long counter = jedis.incr(key);

        // 补全递增数值为5位
        String paddedCounter = String.format("%05d", counter);
        // 生成带日期和递增数的键名
        String keyWithCounter = dateString + paddedCounter;


        String dateString1 = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        String key2 = "Base-Service-DateTime2:" + dateString1;

        Long increment = redisTemplate.opsForValue().increment(key2);
        // 补全递增数值为5位
        String paddedCounter1 = String.format("%05d", increment);
        // 生成带日期和递增数的键名
        String keyWithCounter1 = dateString1 + paddedCounter1;


        String key3 = "Base-Service-DateTime3:" + dateString;
        long l1 = redisson.getAtomicLong(key3).incrementAndGet();

        // 补全递增数值为5位
        String paddedCounter2 = String.format("%05d", l1);
        // 生成带日期和递增数的键名
        String keyWithCounter2 = dateString + paddedCounter2;

        log.info("keyWithCounter1:"+keyWithCounter);
        log.info("keyWithCounter2:"+keyWithCounter1);
        log.info("keyWithCounter3:"+keyWithCounter2);

        return null;
    }

    @Test
    public void testNumber(){
        this.YearMonthDayNumber();
    }


}
