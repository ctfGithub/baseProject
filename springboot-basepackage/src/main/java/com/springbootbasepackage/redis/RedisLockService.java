package com.springbootbasepackage.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;

public class RedisLockService {

//    SetParams参数定义
//    EX seconds − 设置指定的到期时间(以秒为单位)。
//    PX milliseconds - 设置指定的到期时间(以毫秒为单位)。
//    NX - 仅在键不存在时设置键。
//    XX - 只有在键已存在时才设置。


    private static JedisPool jedisPool;

    //uuid，用于区分不同客户端
    private String uid;

    //锁的名称（key）
    private String lockName;

    //锁过期时间(防止死锁)
    private Integer expireTime;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(200);
        // 设置最大空闲数
        config.setMaxIdle(8);
        // 设置最大等待时间
        config.setMaxWaitMillis(1000 * 100);
        // 在borrow一个jedis实例时，是否需要验证，若为true，则所有jedis实例均是可用的
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config, "127.0.0.1", 6379, 3000, null);
    }

    public RedisLockService(String lockName, Integer expireTime) {
        this.lockName = lockName;
        this.expireTime = (expireTime == null || expireTime <= 0) ? 100 : expireTime;
        this.uid = UUID.randomUUID().toString();
    }

    /**
     * @描述: 获取锁
     * @作者:
     * @时间:
     */
    public Boolean getLock(String lockName,Integer expireTime) {
        Jedis jedisClient = jedisPool.getResource();
        try {
            //用uid加上线程id表示value，用于锁释放判断是否是当前线程获取到的锁
            String lockValue = uid + Thread.currentThread().getId();
            //用nx命令设置key并加上过期时间
            String result = jedisClient.set(lockName, lockValue, new SetParams().ex(expireTime) );

            return "ok".equalsIgnoreCase(result);
        } catch (Exception e) {
            //操作异常
            e.printStackTrace();
            return false;
        } finally {
            if (jedisClient != null) {
                jedisClient.close();
            }
        }
    }

    /**
     * @描述: 释放锁
     * @作者:
     * @时间:
     */
    public void releaseLock() {
        String lockValue = uid + Thread.currentThread().getId();
        Jedis jedisClient = jedisPool.getResource();
        try {
            String result = jedisClient.get(lockName);
            if (lockValue.equalsIgnoreCase(result)) {
                //是当前线程获取到的锁，释放
                jedisClient.del(lockName);
            }
        } catch (Exception e) {

        } finally {
            if (jedisClient != null) {
                jedisClient.close();
            }
        }

    }


}
