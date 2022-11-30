package com.springbootbasepackage.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Configuration
@EnableAsync
public class AsyncTaskPoolConfig {
    @Bean("taskExecutor")
    public Executor taskExecutor() {
        int i = Runtime.getRuntime().availableProcessors();//获取服务器内核数
        System.out.println("系统最大线程数：" + i);
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(i);
        taskExecutor.setMaxPoolSize(i);
        taskExecutor.setQueueCapacity(99999);
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setThreadNamePrefix("taskExecutor--");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(60);
        return taskExecutor;
    }


    @Bean("taskExecutors")
    public ThreadPoolExecutor taskExecutors() {
        int i = Runtime.getRuntime().availableProcessors();//获取服务器内核数
        System.out.println("系统最大线程数：" + i);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                i,
                i,
                5,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(Integer.MAX_VALUE),
                new NamedThreadFactory("execl导出线程池"),
                new ThreadPoolExecutor.DiscardPolicy())
                ;
        System.out.println("execl导出线程池初始化完毕-------------");
        return threadPoolExecutor;
    }
}
