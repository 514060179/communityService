package com.newland.property.job.jpush;

import com.newland.property.core.jpush.JPushMessageQueue;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.msg.JPushMessageDto;
import com.newland.property.utils.factory.ApplicationContextFactory;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Moonny
 */
public class JPushMessageExecutor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(JPushMessageExecutor.class);

    private IJPushMessageAdapt ijPushMessageAdapt;

    private static final int MAX_ROW = 200;

    //默认线程大小
    private static final int DEFAULT_EXPORT_POOL = 4;

    private boolean isRun = false;

    public JPushMessageExecutor(boolean isRun) {
        this.isRun = isRun;
    }

    @Override
    public void run() {
        while (isRun) {
            logger.debug("导入数据线程开始处理");
            try {
                doPushMessage();
            } catch (Throwable e) {
                logger.error("处理消息异常", e);
                e.printStackTrace();
            }
            logger.debug("导入数据线程处理完成");
        }
    }

    private void doPushMessage() throws Exception {
        JPushMessageDto jPushMessageDto = JPushMessageQueue.getData();
        if (jPushMessageDto == null) {
            return;
        }

        if (ijPushMessageAdapt == null) {
            ijPushMessageAdapt = ApplicationContextFactory.getBean("jpushMessageService", IJPushMessageAdapt.class);
        }

        if (ijPushMessageAdapt != null) {
            ijPushMessageAdapt.pushMessage(jPushMessageDto);
        }
    }

    /**
     * 线程启动器
     */
    public static void startJPushMessageExecutor() {
        logger.debug("开始初始化jpush队列");
        ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_EXPORT_POOL);
        executorService.execute(new JPushMessageExecutor(true));
        logger.debug("初始化jpush队列完成");
    }
}
