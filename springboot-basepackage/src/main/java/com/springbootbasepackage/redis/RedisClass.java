package com.springbootbasepackage.redis;

import redis.clients.jedis.Jedis;


/**
 * 连接redis
 */
public class RedisClass {
    public static void main(String[] args) {
        //实例化一个客户端
        Jedis jedis = new Jedis("127.0.0.1");
        //ping下，看看是否通的
        System.out.println("Server is running: " + jedis.ping());
        //保存一个
        jedis.set("leiTest1", "localhost Connection  sucessfully");
        //获取一个
        String leite=jedis.get("leiTest");
        System.out.println("leiTest键值为: " +leite);
    }


}
