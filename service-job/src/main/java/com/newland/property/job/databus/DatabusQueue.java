package com.newland.property.job.databus;

public class DatabusQueue {

    public void initExportQueue(){
        //启动导出数据线程处理器
        DatabusDataExecutor.startQueueDataExecutor();
    }
}
