package com.example.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池分发执行任务
 */
public class Dispatcher {

    private ExecutorService executorService;

    public Dispatcher(){
        executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setDaemon(false);
                thread.setName("okHttp Thread");
                return thread;
            }
        });
    }

    public void execute(Runnable call){
        executorService.execute(call);
    }
}
