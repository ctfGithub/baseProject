package com.springbootbasepackage.configuration;

import com.alibaba.fastjson.JSONObject;
import com.springbootbasepackage.dto.LoginIphoneAndYzmDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        response.setCharacterEncoding("utf-8");
        String requestURI = request.getRequestURI();
        log.info("url:{}",requestURI);

        String token = request.getHeader("token");
        if (token != null) {
            //验证token
            String str = (String) redisTemplate.opsForValue().get(token);
            LoginIphoneAndYzmDTO dto =JSONObject.parseObject(str,LoginIphoneAndYzmDTO.class);

            //boolean result = TokenUtil.verify(token);
            if (Objects.nonNull(dto)) {
                log.info("通过拦截器");
                //重新续期 30分钟
                redisTemplate.opsForValue().set(token, JSONObject.toJSONString(dto),30L, TimeUnit.MINUTES);
                return true;
            }
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            JSONObject json = new JSONObject();
            json.put("success", "false");
            json.put("msg", "认证失败，请重新登陆");
            json.put("code", "500");
            response.getWriter().append(json.toJSONString());
            log.error("认证失败，未通过拦截器");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
            response.sendRedirect("/loginController/login/account");
            return false;
        }
        return false;

    }
}
