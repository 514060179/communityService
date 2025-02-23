package com.newland.property.job.importData;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportQueueConfig {


    @Bean
    public ImportQueue importQueue(){
        ImportQueue importQueue = new ImportQueue();
        importQueue.initExportQueue();
        return importQueue;
    }
}
