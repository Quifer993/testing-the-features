package ru.zolo.ttl.async;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "ttlTaskExecutor")
    public Executor ttlTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.setThreadNamePrefix("TTL-Executor-");
        executor.initialize();

        return TtlExecutors.getTtlExecutorService(executor.getThreadPoolExecutor());
    }
}
