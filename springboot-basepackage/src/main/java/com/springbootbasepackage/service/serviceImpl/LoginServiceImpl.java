package com.springbootbasepackage.service.serviceImpl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootbasepackage.base.SntException;
import com.springbootbasepackage.dto.LoginIphoneAndYzmDTO;
import com.springbootbasepackage.dto.LoginIphoneDTO;
import com.springbootbasepackage.dto.UserDTO;
import com.springbootbasepackage.redis.SedissonManage;
import com.springbootbasepackage.service.LoginService;
import com.springbootbasepackage.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Resource
    private SedissonManage sedissonManage;


    @Override
    public String sendIphone(LoginIphoneDTO loginIphoneDTO) {
        Random randObj = new Random();
        String yzm = Integer.toString(100000 + randObj.nextInt(900000));
        //验证码  放在Redis 中 ,验证码时间 1 min
        redisTemplate.opsForValue().set(loginIphoneDTO.getIphone()+"yzm",yzm,2L, TimeUnit.MINUTES);
        return yzm;
    }


    @SneakyThrows
    @Override
    public LoginIphoneAndYzmDTO login(LoginIphoneAndYzmDTO loginIphoneAndYzmDTO) {
        RLock lock = sedissonManage.lock("login:"+loginIphoneAndYzmDTO.getIphone());
        if(!lock.tryLock()){
            throw new SntException("请稍后重试");
        }
        try{
            UserDTO user = new UserDTO();
            user.setIphone(loginIphoneAndYzmDTO.getIphone());
            if(StringUtils.isNotBlank(loginIphoneAndYzmDTO.getYzm())){
                //校验验证码
                String yzm = (String) redisTemplate.opsForValue().get(loginIphoneAndYzmDTO.getIphone()+"yzm");
                if(!loginIphoneAndYzmDTO.getYzm().equals(yzm)){
                    throw new SntException("验证码不正确");
                }
            }

            List<UserDTO> users = userService.queryByUser(user);
            if(CollUtil.isEmpty(users)){
                throw new SntException("查询不到用户信息");
            }else{
                if(loginIphoneAndYzmDTO.getIphone() != null && loginIphoneAndYzmDTO.getYzm() != null) {
                    //生成token
                    //String token = TokenUtil.sign(loginIphoneAndYzmDTO.getIphone(), loginIphoneAndYzmDTO.getYzm());
                    UUID uuid= UUID.randomUUID();
                    String token = uuid.toString();
                    loginIphoneAndYzmDTO.setToken(token);
                    log.info("token:{}",token);
                    //断言token不为空，并以用户名作为key，存入redis
                    Assert.notNull(token,"登录失败了，口令生成失败");
                    String getToken = (String) redisTemplate.opsForValue().get("shoujihao:"+loginIphoneAndYzmDTO.getIphone());
                    if(StringUtils.isNotBlank(getToken)){
                        //单点登录 ，删除原来的 token
                        redisTemplate.delete(getToken);
                    }
                    redisTemplate.opsForValue().set("shoujihao:"+loginIphoneAndYzmDTO.getIphone(),token,30L, TimeUnit.MINUTES);
                    redisTemplate.opsForValue().set(token, JSONObject.toJSONString(loginIphoneAndYzmDTO),30L, TimeUnit.MINUTES);
                    //redisTemplate.opsForValue().set(token, objectMapper.writeValueAsString(loginIphoneAndYzmDTO),30L, TimeUnit.MINUTES);

                    return loginIphoneAndYzmDTO;
                }else{
                    return loginIphoneAndYzmDTO;
                }
            }

        }finally {
            lock.unlock();
        }
    }


}
