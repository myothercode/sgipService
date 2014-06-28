package com.sgip.domain;

import com.sgip.domain.VO.SMSBody;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: chace.cai
 * Date: 13-10-16
 * Time: 上午11:08
 * To change this template use File | Settings | File Templates.
 */
public class QueueAndPools {
   // public static ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(2);//定时获取数据的线程
   // public static ExecutorService pool= Executors.newFixedThreadPool(5);//执行任务的线程
    public final static BlockingQueue<SMSBody> smsQueue = new ArrayBlockingQueue<SMSBody>(30);//待发送的短信队列
    public static AtomicInteger atomicInteger = new AtomicInteger(1);//计数器 int n= DicomQueue.atomicInteger.addAndGet(1);  if(n>=100)DicomQueue.atomicInteger.set(1);
    public static TaskExecutor taskExecutor;  //定时获取数据的线程

    //public static AtomicInteger incram = new AtomicInteger(1);//判断线程是否还在运行
    //public static AtomicInteger isRuning = new AtomicInteger(1);//判断线程是否还在运行
}
