package com.wushengju.study.concurrency.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发测试类
 *
 * @author Sunny
 * @version 1.0
 * @className ConcurrencyTest
 * @date 2019-12-09 13:45
 */
@Slf4j
public class ConcurrencyTest1 {
    /**
     * 请求总数
     */
    public static int clientTotal = 5000;
    /**
     * 线程总数
     */
    public static int threadTotal = 200;

    public static AtomicInteger count = new AtomicInteger(0);


    private static void add() {
        count.incrementAndGet();
    }

    public static void main(String[] args) throws Exception{
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (Exception e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("count的值为:{}", count.get());
    }

}
