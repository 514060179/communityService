package com.newland.property.job.jpush;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Moonny
 */
@Configuration
public class JPushQueueConfig {
    @Bean
    public JPushQueue jPushQueue(){
        JPushQueue jPushQueue = new JPushQueue();
        jPushQueue.initJPushQueue();
        return jPushQueue;
    }
}
