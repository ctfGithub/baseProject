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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public LoginIphoneAndYzmDTO login(LoginIphoneAndYzmDTO loginIphoneAndYzmDTO) {
        String phone = loginIphoneAndYzmDTO.getIphone();
        String lockKey = "login:" + phone;

        // ======================
        // 🔥 看门狗最优加锁（自动续期、不会死锁、高可用）
        // ======================
        RLock lock = redissonClient.getLock(lockKey);
        boolean lockSuccess = false;

        try {
            // 看门狗机制：
            // waitTime = 2秒   等待锁超时
            // 不设置 leaseTime  → 自动开启看门狗（每30秒续期成30秒）
            lockSuccess = lock.tryLock(2, TimeUnit.SECONDS);

            // 获取不到锁直接返回
            if (!lockSuccess) {
                throw new SntException("操作频繁，请稍后再试");
            }

            // ========= 下面是你的业务逻辑（完全不变） ==========
            UserDTO user = new UserDTO();
            user.setIphone(phone);

            if (StringUtils.isNotBlank(loginIphoneAndYzmDTO.getYzm())) {
                String yzm = (String) redisTemplate.opsForValue().get(phone + "yzm");
                if (StringUtils.isBlank(yzm)) {
                    throw new SntException("请重新发送验证码");
                }
                if (!loginIphoneAndYzmDTO.getYzm().equals(yzm)) {
                    throw new SntException("验证码不正确");
                }
            }

            List<UserDTO> users = userService.queryByUser(user);
            if (CollUtil.isEmpty(users)) {
                throw new SntException("查询不到用户信息");
            }

            if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(loginIphoneAndYzmDTO.getYzm())) {
                UUID uuid = UUID.randomUUID();
                String token = uuid.toString();
                loginIphoneAndYzmDTO.setToken(token);

                Assert.notNull(token, "登录失败，口令生成失败");

                String oldToken = (String) redisTemplate.opsForValue().get("shoujihao:" + phone);
                if (StringUtils.isNotBlank(oldToken)) {
                    redisTemplate.delete(oldToken);
                }

                redisTemplate.opsForValue().set("shoujihao:" + phone, token, 30L, TimeUnit.MINUTES);
                redisTemplate.opsForValue().set(token, JSONObject.toJSONString(loginIphoneAndYzmDTO), 30L, TimeUnit.MINUTES);
            }

            return loginIphoneAndYzmDTO;

        } catch (InterruptedException e) {
            // 锁等待被中断
            Thread.currentThread().interrupt();
            throw new SntException("系统繁忙，请稍后再试");
        } finally {
            // ======================
            // 🔥 必须判断：锁是当前线程持有，才解锁
            // ======================
            if (lockSuccess && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
