package com.wushengju.study.test;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * guavamap测试类
 *
 * @author Sunny
 * @version 1.0
 * @className GuavaMapTest
 * @date 2019-12-13 09:02
 */
@Slf4j
public class GuavaMapTest {

    private static Map<String, String> map = Maps.newHashMap();
    private static ReentrantLock reentrantLock = new ReentrantLock();
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                reentrantLock.lock();
                try {
                    Thread.sleep(3000);
                } catch (Exception e){
                    log.error("error", e);
                } finally {
                    reentrantLock.unlock();
                }
            });
           thread.start();
        }
    }

}
