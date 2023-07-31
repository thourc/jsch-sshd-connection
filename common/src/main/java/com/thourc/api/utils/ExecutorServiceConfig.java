package com.rogers.api.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Slf4j
@Configuration
public class ExecutorServiceConfig {
    @Bean
    public ExecutorService executorService(@Value("${application.tasks.concurrency:4}") int size) {
        return new ThreadPoolExecutor(size, size, 0L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(size),
                Executors.defaultThreadFactory(),
                (task, executor) -> {
                    try {
                        executor.getQueue().put(task);
                    } catch (InterruptedException e1) {
                        log.error("Work discarded, thread was interrupted while waiting for space to schedule: {}", task);
                        Thread.currentThread().interrupt();
                    }
                });
    }
}
