package com.springbootbasepackage.base;


import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

//主键id生成 按照雪花算法（速度快）
public class IdGenerator {
    /**
     * 起始的时间戳
     */
    private static final long START_TIME_MILLIS;

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long WORKID_BIT = 10;   //机器标识占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_WORK_NUM = -1L ^ (-1L << WORKID_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long WORKID_SHIFT = SEQUENCE_BIT;
    private final static long TIMESTMP_SHIFT = WORKID_SHIFT + WORKID_BIT;

    private long sequence = 0L; //序列号
    private long lastStmp = -1L;

    /** workId */
    private long workId;

    static {
        String startDate = "2018-01-01 00:00:00";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(startDate, df);
        START_TIME_MILLIS = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();

    }




    /**
     * 获取分部署式发号器
     * @param workId 每台服务需要传一个服务id
     * @return
     */
    public static synchronized IdGenerator getDistributedIdGenerator(long workId) {
        return new IdGenerator(workId);
    }

    public static synchronized IdGenerator getStandAloneIdGenerator() {
        long workId = MAX_WORK_NUM;
        return new IdGenerator(workId);
    }


    private IdGenerator(long workId) {
        if (workId > MAX_WORK_NUM || workId <= 0) {
            throw  new RuntimeException("workdId的值设置错误");
        }
        this.workId = workId;
    }

    /**
     * 生成id
     * @return
     */
    public synchronized long nextId() {
        long currStmp = System.currentTimeMillis();
        if (currStmp < START_TIME_MILLIS) {
            throw new RuntimeException("机器时间存在问题，请注意查看");
        }

        if (currStmp == lastStmp) {
            sequence =  (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L) {
                currStmp = getNextMillis(currStmp);
            }
        } else {
            sequence = 0L;
        }
        lastStmp = currStmp;

        return ((currStmp - START_TIME_MILLIS) << TIMESTMP_SHIFT)
                | (workId << WORKID_SHIFT)
                | (sequence);
    }

    public long getNextMillis(long currStmp) {
        long millis = System.currentTimeMillis();
        while (millis <= currStmp) {
            millis = System.currentTimeMillis();
        }
        return millis;
    }

    /**
     * 获取最大的工作数量
     * @return
     */
    public static long getMaxWorkNum() {
        return MAX_WORK_NUM;
    }

    public static void main(String[] args) {
        IdGenerator idGenerator1 = IdGenerator.getDistributedIdGenerator(1);
        for (int i = 0; i < 200; i++) {
            System.out.println(idGenerator1.nextId());
        }

    }

}
