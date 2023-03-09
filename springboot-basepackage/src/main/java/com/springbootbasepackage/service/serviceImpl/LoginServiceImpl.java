package com.springbootbasepackage.service.serviceImpl;

import com.springbootbasepackage.base.SntException;
import com.springbootbasepackage.dto.LoginIphoneAndYzmDTO;
import com.springbootbasepackage.dto.LoginIphoneDTO;
import com.springbootbasepackage.redis.RedisUtil;
import com.springbootbasepackage.service.LoginService;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public String sendIphone(LoginIphoneDTO loginIphoneDTO) {
        Random randObj = new Random();
        String yzm = Integer.toString(100000 + randObj.nextInt(900000));
        //验证码  放在Redis 中
        redisUtil.set(loginIphoneDTO.getIphone(),yzm);
        return yzm;
    }

    @Override
    public String tokenCreate(LoginIphoneAndYzmDTO loginIphoneAndYzmDTO) {
        String yzm = (String) redisUtil.get(loginIphoneAndYzmDTO.getIphone());
        if(!loginIphoneAndYzmDTO.getYzm().equals(yzm)){
            throw new SntException("手机号和验证码校验失败，请重新发送验证码");
        }
        //生成token
        UUID uuid= UUID.randomUUID();
        String token = uuid.toString();
        redisUtil.set("login:"+loginIphoneAndYzmDTO.getIphone(),token);
        return token;
    }


}
