package com.powernode.util;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 获取线程池
 *
 * @author DuBo
 * @createDate 2022/7/26 17:45
 */
public class ManagerThreadPoolUtil {
    public static ThreadPoolExecutor getThreadPool(){
        return new ThreadPoolExecutor(
                4,
                Runtime.getRuntime().availableProcessors(),
                30,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(30),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
