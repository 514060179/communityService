package com.newland.property.job.export;

public class ExportQueue {

    public void initExportQueue(){
        //启动导出数据线程处理器
        ExportDataExecutor.startExportDataExecutor();
    }
}
