package com.rogers.api.utils;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Value
@Component
public class TaskService {

    ExecutorService tasks;

    public Future<?> submit(Runnable task) {
        return tasks.submit(task);
    }

    public void shutdown() {
        log.warn("Graceful shutdown. Stop accepting new tasks, complete remaining.");
        tasks.shutdown();
        try {
            if (!tasks.awaitTermination(30, TimeUnit.SECONDS)) {
                log.error("Unable to gracefully shut down within 30s");
            }
        } catch (InterruptedException e) {
            log.error("Unable to gracefully shut down within 30s", e);
            Thread.currentThread().interrupt();
        }
    }
}
