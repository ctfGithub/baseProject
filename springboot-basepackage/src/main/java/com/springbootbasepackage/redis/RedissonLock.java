package com.springbootbasepackage.redis;

import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

public class RedissonLock {

    @Test
    public void redissonLock(){
        boolean  s = false;
        //枷锁的第一种方法，Redisson枷锁
        Config config = new Config();

        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
//        config.useClusterServers().addNodeAddress("redis://127.0.0.1:6379").addNodeAddress("redis://127.0.0.1:6379").setPassword("").setScanInterval(1000);

        RedissonClient redissonClient = Redisson.create(config);
        RedissonReactiveClient reactive = Redisson.createReactive(config);
        RLock lock = redissonClient.getLock("redis+key+ceshi");
//        尝试加锁，最多等待3秒，上锁以后10秒自动解锁
        try {
            boolean res = lock.tryLock(3, 600, TimeUnit.SECONDS);
            if(res){    //成功
                System.out.println("你好");
                s= true;
                System.out.println(s);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
            reactive.shutdown();
        }
    }



}
