package com.springbootbasepackage.util;

import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RequestIdManager {
    //private static ThreadLocal<String> requestIdContext = new ThreadLocal<>();

    /**
     * 1 从线程变量中取出，如果有则直接返回
     * 2 从request头中取出，如果没有则生成
     * 3.塞入MDC,和线程变量中
     */
    public static String getRequestId() {
        String requestId = null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = requestAttributes.getRequest();
            if (request.getAttribute("requestId") != null) {
                return request.getAttribute("requestId").toString();
            }
            requestId = request.getHeader("requestId");  // 从请求头里取出
            if (Objects.isNull(requestId)) {
                requestId = UUID.randomUUID().toString();
                request.setAttribute("requestId", requestId);
            }
        }
        return requestId;
    }

    public static void setSonMDCInfo(List<String> fatherMdcInfo) {
        MDC.put("requestId", fatherMdcInfo.get(0));
        MDC.put("ip", fatherMdcInfo.get(1));
        MDC.put("mpId", fatherMdcInfo.get(2));
        MDC.put("unionid", fatherMdcInfo.get(3));
        MDC.put("openid", fatherMdcInfo.get(4));
    }

}
