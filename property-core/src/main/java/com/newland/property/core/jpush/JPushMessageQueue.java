package com.newland.property.core.jpush;

import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.msg.JPushMessageDto;
import org.slf4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Moonny
 */
public class JPushMessageQueue {

    private static final Logger log = LoggerFactory.getLogger(JPushMessageQueue.class);

    private static final BlockingQueue<JPushMessageDto> msgs = new LinkedBlockingQueue<>(100);

    /**
     * 添加消息
     *
     * @param jPushMessageDto
     */
    public static void addMsg(JPushMessageDto jPushMessageDto) {
        try {
            msgs.offer(jPushMessageDto, 3, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("写入队列失败", e);
            e.printStackTrace();
        }
    }

    public static JPushMessageDto getData() {
        try {
            return msgs.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
