package com.springbootbasepackage.redis;


import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient getRedisSon() {
        Config config = new Config();
        String address = new StringBuilder("redis://").append("127.0.0.1").append(":").append("6379").toString();
        config.useSingleServer().setAddress(address);
        //if (null != password && !"".equals(password.trim())) {
        //    config.useSingleServer().setPassword(password);
        //}
        return Redisson.create(config);

    }
}
