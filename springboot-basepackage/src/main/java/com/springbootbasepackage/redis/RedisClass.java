package com.springbootbasepackage.redis;

import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.concurrent.TimeUnit;


/**
 * 连接redis
 */
public class RedisClass {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisLockService redisLockService;

    @Autowired
    private SedisManage redisManager;



    @Test
    public void test() {
        //实例化一个客户端
        Jedis jedis = new Jedis("127.0.0.1");
        //ping下，看看是否通的
        System.out.println("redis运行状态: " + jedis.ping());


        //设置永久性的key-value，不用添加时间处理
        jedis.set("leiTest", "测试使用");

        //设置key 10 s
        String result = jedis.set("leiTestTime", "设置这个key为10s", new SetParams().ex(10));
        System.out.println("result: " + result);

        //枷锁的第一种方法，Redisson枷锁
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redissonClient = Redisson.create(config);
        RedissonReactiveClient reactive = Redisson.createReactive(config);
        RLock lock = redissonClient.getLock("redis+key+ceshi");
//        尝试加锁，最多等待3秒，上锁以后10秒自动解锁
        try {
            boolean res = lock.tryLock(3, 600, TimeUnit.SECONDS);
            if(res){    //成功
                System.out.println("你好");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
            reactive.shutdown();
        }


        





    }


}
