package com.springbootbasepackage.service.serviceImpl;

import cn.hutool.core.collection.CollUtil;
import com.springbootbasepackage.base.SntException;
import com.springbootbasepackage.dto.LoginIphoneAndYzmDTO;
import com.springbootbasepackage.dto.LoginIphoneDTO;
import com.springbootbasepackage.dto.UserDTO;
import com.springbootbasepackage.redis.RedisUtil;
import com.springbootbasepackage.service.LoginService;
import com.springbootbasepackage.service.UserService;
import com.springbootbasepackage.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RedisTemplate redisTemplate;


    @Resource
    private UserService userService;


    @Override
    public String sendIphone(LoginIphoneDTO loginIphoneDTO) {
        Random randObj = new Random();
        String yzm = Integer.toString(100000 + randObj.nextInt(900000));
        //验证码  放在Redis 中
        redisUtil.set(loginIphoneDTO.getIphone()+"yzm",yzm);
        return yzm;
    }


    @Override
    public LoginIphoneAndYzmDTO login(LoginIphoneAndYzmDTO loginIphoneAndYzmDTO) {
        UserDTO user = new UserDTO();
        user.setIphone(loginIphoneAndYzmDTO.getIphone());
        if(StringUtils.isNotBlank(loginIphoneAndYzmDTO.getYzm())){
            //校验验证码
            String yzm = (String) redisUtil.get(loginIphoneAndYzmDTO.getIphone()+"yzm");
            if(!loginIphoneAndYzmDTO.getYzm().equals(yzm)){
                throw new SntException("验证码不正确");
            }
        }

        List<UserDTO> users = userService.queryByUser(user);
        if(CollUtil.isEmpty(users)){
            return loginIphoneAndYzmDTO;
        }else{
            if(loginIphoneAndYzmDTO.getIphone() != null && loginIphoneAndYzmDTO.getYzm() != null) {
                String token = TokenUtil.sign(loginIphoneAndYzmDTO.getIphone(), loginIphoneAndYzmDTO.getYzm());
                loginIphoneAndYzmDTO.setToken(token);
                log.info("token:{}",token);
                //断言token不为空，并以用户名作为key，存入redis
                assert token != null;

                //生成token
                //UUID uuid= UUID.randomUUID();
                //String token = uuid.toString();
                redisTemplate.opsForValue().set(loginIphoneAndYzmDTO.getIphone(),token);
                return loginIphoneAndYzmDTO;
            }else{
                return loginIphoneAndYzmDTO;
            }
        }
    }






}
