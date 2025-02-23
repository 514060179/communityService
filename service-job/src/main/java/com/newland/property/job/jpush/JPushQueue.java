package com.newland.property.job.jpush;

public class JPushQueue {

    public void initJPushQueue() {
        //启动jpush线程处理器
        JPushMessageExecutor.startJPushMessageExecutor();
    }
}
