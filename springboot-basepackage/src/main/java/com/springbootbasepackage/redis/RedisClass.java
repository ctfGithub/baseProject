package com.springbootbasepackage.redis;

import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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


    @Test
    public void test() {
        //实例化一个客户端
        Jedis jedis = new Jedis("127.0.0.1");
        //ping下，看看是否通的
        System.out.println("Server is running: " + jedis.ping());
        //保存一个
        jedis.set("leiTest", "localhost Connection  sucessfully");
        //获取一个
        String leite = jedis.get("leiTest");
        System.out.println("leiTest键值为: " + leite);


        //设置key 10 s
        jedis.set("leiTestTime", "设置这个key为10s", new SetParams().ex(60));



    }


}
