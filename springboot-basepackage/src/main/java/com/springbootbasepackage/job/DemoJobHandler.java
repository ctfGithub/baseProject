package com.springbootbasepackage.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
@Component
public class DemoJobHandler {

        @XxlJob("demoJobHandler")
        public void demoJobHandler() throws Exception {
                //获取当前的年月日时分秒
                String dateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                System.out.println("demoJobHandler当前时间的年月日时分秒:"+dateTime);
        }


}
