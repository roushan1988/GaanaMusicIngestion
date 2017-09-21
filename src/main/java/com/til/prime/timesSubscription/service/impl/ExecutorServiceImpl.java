package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.service.ExecutorService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExecutorServiceImpl implements ExecutorService {
    private static final Logger LOG = Logger.getLogger(ExecutorServiceImpl.class);
    private final java.util.concurrent.ExecutorService executorService;

    private ExecutorServiceImpl() {
        int corePoolSize = 20;
        int maxPoolSize = 400;
        long keepAliveTime = 2L;
        final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(50000);

        executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MINUTES, workQueue, new ThreadFactory() {
            final AtomicInteger threadCounter = new AtomicInteger();
            public Thread newThread(Runnable r) {
                return new Thread(null, r, "Executor-" + threadCounter.incrementAndGet());
            }
        }, new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                LOG.error("Executor queue is full");
                throw new RejectedExecutionException("Executor queue is full");
            }
        });
    }

    public java.util.concurrent.ExecutorService getExecutorService() {
        return executorService;
    }

    public void destroy() throws Exception {
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
