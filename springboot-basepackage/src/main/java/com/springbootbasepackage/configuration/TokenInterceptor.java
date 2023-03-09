package com.springbootbasepackage.configuration;

import com.alibaba.fastjson.JSONObject;
import com.springbootbasepackage.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

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
        if (token != null || requestURI.contains("swagger") ||  requestURI.contains("/favicon.ico")) {
            //验证token
            if(requestURI.contains("swagger") ||  requestURI.contains("/favicon.ico")){
                log.error("通过拦截器");
                return true;
            }else{
                boolean result = TokenUtil.verify(token);
                if (result) {
                    log.error("通过拦截器");
                    return true;
                }
            }
        }

        //if (token != null) {
        //    //验证token
        //    boolean result = TokenUtil.verify(token);
        //    if (result) {
        //        log.error("通过拦截器");
        //        return true;
        //    }
        //}
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            JSONObject json = new JSONObject();
            json.put("success", "false");
            json.put("msg", "认证失败，未通过拦截器");
            json.put("code", "500");
            response.getWriter().append(json.toJSONString());
            log.error("认证失败，未通过拦截器");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
            return false;
        }
        return false;

    }
}
