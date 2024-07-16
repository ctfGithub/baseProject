package com.springbootbasepackage.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
public class RequestIdInterceptor implements RequestInterceptor {
    public static final String TRACE_ID_HEADER = "requestId";

    private static final ThreadLocal<String> requestId = new ThreadLocal<>();

    @Override
    public void apply(RequestTemplate template) {
        //重请求头中获取，如果没有就新建一个放在缓存中，当前的session 中用相同的

        // 假设traceId是从某个地方获取，这里只是示例
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 从请求头中获取requestId
        String requestId  = (String) request.getAttribute("requestId");
        if(StringUtils.isBlank(requestId)){
            requestId = UUID.randomUUID().toString(); // 应该从合适的上下文获取或生成
        }
        //requestId 放在 请求头中
        request.setAttribute("requestId",requestId);
        log.info("requestId:{}",requestId);
        template.header(TRACE_ID_HEADER, requestId);
    }

    public static void setRequestID(String id) {
        requestId.set(id);
    }

    public static String getRequestID() {
        return requestId.get();
    }

}
