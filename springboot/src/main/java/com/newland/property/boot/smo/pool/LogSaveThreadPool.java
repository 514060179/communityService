package com.newland.property.boot.smo.pool;


import com.newland.property.po.log.TransactionLogPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * 功率线程池
 * @author fengtianying
 * @date 2018/5/14 11:59
 */
@Component
public class LogSaveThreadPool implements BeanFactoryAware {

    protected Logger logger = LoggerFactory.getLogger(LogSaveThreadPool.class);

    private BeanFactory factory;//用于从IOC里取对象
    // 线程池维护线程的最少数量
    private final static int CORE_POOL_SIZE = 20;
    // 线程池维护线程的最大数量
    private final static int MAX_POOL_SIZE = 50;
    // 线程池维护线程所允许的空闲时间
    private final static int KEEP_ALIVE_TIME = 0;
    // 线程池所使用的缓冲队列大小
    private final static int WORK_QUEUE_SIZE = 100;
    // 等待消息队列
    private final static int WAIT_QUEUE_SIZE = 5000000;
    // 消息缓冲队列
    Queue<Object> msgQueue = new LinkedList<Object>();

    //由于超出线程范围和队列容量而使执行被阻塞时所使用的处理程序
    final RejectedExecutionHandler handler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            LogSaveDbThread logSaveDbThread = (LogSaveDbThread) r;
            TransactionLogPo transactionLogPo = logSaveDbThread.getTransactionLogPo();
            logger.warn("太忙了,交给调度线程池逐一处理" + transactionLogPo);
            msgQueue.offer(transactionLogPo);
        }
    };

    // 线程池
    final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.SECONDS, new ArrayBlockingQueue(WORK_QUEUE_SIZE), this.handler);

    // 调度线程池。此线程池支持定时以及周期性执行任务的需求。
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    // 访问消息缓存的调度线程,每秒执行一次
    // 查看是否有待定请求，如果有，则创建一个新的AccessDBThread，并添加到线程池中
    final ScheduledFuture taskHandler = scheduler.scheduleAtFixedRate(new Runnable() {
        @Override
        public void run() {
            if (!msgQueue.isEmpty()) {
                if (threadPool.getQueue().size() < WAIT_QUEUE_SIZE) {
                    TransactionLogPo param = (TransactionLogPo) msgQueue.poll();
                    LogSaveDbThread periodDbThread = (LogSaveDbThread) factory.getBean("logSaveDbThread");
                    periodDbThread.setTransactionLogPo(param);
                    threadPool.execute(periodDbThread);
                }else{
                    logger.error("回调线程池队列已满queue size={}", threadPool.getQueue().size());
                }
            }
        }
    }, 0, 1000, TimeUnit.MILLISECONDS);

    //终止订单线程池+调度线程池
    public void shutdown() {
        //true表示如果定时任务在执行，立即中止，false则等待任务结束后再停止
        logger.info("定时任务在执行，立即中止。是否成功={}", taskHandler.cancel(false));
        scheduler.shutdown();
        threadPool.shutdown();
    }

    public Queue<Object> getMsgQueue() {
        return msgQueue;
    }


    //将任务加入线程池
    public void processOrders(TransactionLogPo param) {
        LogSaveDbThread periodDbThread = (LogSaveDbThread) factory.getBean("logSaveDbThread");
        periodDbThread.setTransactionLogPo(param);
        threadPool.execute(periodDbThread);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        factory = beanFactory;
    }
}
