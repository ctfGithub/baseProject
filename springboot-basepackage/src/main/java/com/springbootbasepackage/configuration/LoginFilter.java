package com.springbootbasepackage.configuration;

import com.alibaba.fastjson.JSONObject;
import com.springbootbasepackage.dto.LoginIphoneAndYzmDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
//@Component
public class LoginFilter implements Filter {

    @Resource
    private RedisTemplate redisTemplate;

    List<String> paths = Arrays.asList("/loginController/login/account", "/loginController/sendIphone",
            "/swagger-ui.html","/null/**","/swagger-resources/**","/swagger/**","/webjars/**");
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        //跨域设置
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String uri = request.getRequestURI();
        log.info("请求地址{}",uri);
        //获取请求路径
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        boolean filterPath = paths.contains(path);
        if(filterPath){
            //直接放行
            filterChain.doFilter(req, res);
        }else{
            //判断 Redis  是否有 token
            String token = request.getHeader("token");
            //验证token
            String str = (String) redisTemplate.opsForValue().get(token);
            LoginIphoneAndYzmDTO dto = JSONObject.parseObject(str,LoginIphoneAndYzmDTO.class);

            //boolean result = TokenUtil.verify(token);
            if (Objects.nonNull(dto)) {
                log.info("通过拦截器");
                //重新续期 30分钟
                redisTemplate.opsForValue().set(token, JSONObject.toJSONString(dto),30L, TimeUnit.MINUTES);
                //直接放行
                filterChain.doFilter(req, res);
            }else{
                log.info("用户没有登录");
                response.setStatus(500);
                response.sendRedirect("");//重定向到登录地址
                response.sendError(500,"用户登录失败");
            }
        }
    }


}
