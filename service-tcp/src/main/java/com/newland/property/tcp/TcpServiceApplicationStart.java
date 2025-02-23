/*
 * Copyright 2017-2020 吴学文 and newland property team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newland.property.tcp;

import com.newland.property.core.annotation.NewlandPropertyCmdDiscovery;
import com.newland.property.core.annotation.NewlandPropertyListenerDiscovery;
import com.newland.property.core.event.service.BusinessServiceDataFlowEventPublishing;
import com.newland.property.core.trace.NewlandPropertyRestTemplateInterceptor;
import com.newland.property.core.client.RestTemplate;
import com.newland.property.core.event.cmd.ServiceCmdEventPublishing;
import com.newland.property.service.init.ServiceStartInit;
import com.newland.property.tcp.server.TCPServer;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;

import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * spring boot 初始化启动类
 *
 * @version v0.1
 * @auther Moonny
 * @date 2024年5月28日
 */
@SpringBootApplication(scanBasePackages = {"com.newland.property.service", "com.newland.property.tcp",
        "com.newland.property.core", "com.newland.property.config.properties.code", "com.newland.property.db","com.newland.property.doc"})
@EnableDiscoveryClient
@NewlandPropertyCmdDiscovery(cmdPublishClass = ServiceCmdEventPublishing.class,
        basePackages = {"com.newland.property.tcp.cmd"})
@NewlandPropertyListenerDiscovery(listenerPublishClass = BusinessServiceDataFlowEventPublishing.class,
        basePackages = {"com.newland.property.tcp.listener"})
@EnableFeignClients(basePackages = {"com.newland.property.intf.user",
        "com.newland.property.intf.order",
        "com.newland.property.intf.community",
        "com.newland.property.intf.common",
        "com.newland.property.intf.job",
        "com.newland.property.intf.store"})
public class TcpServiceApplicationStart {

    private static Logger logger = LoggerFactory.getLogger(TcpServiceApplicationStart.class);

    @Resource
    private NewlandPropertyRestTemplateInterceptor newlandPropertyRestTemplateInterceptor;

    /**
     * 实例化RestTemplate，通过@LoadBalanced注解开启均衡负载能力.
     *
     * @return restTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build(RestTemplate.class);
        restTemplate.getInterceptors().add(newlandPropertyRestTemplateInterceptor);
        return restTemplate;
    }

    /**
     * 实例化RestTemplate
     *
     * @return restTemplate
     */
    @Bean
    public RestTemplate outRestTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build(RestTemplate.class);
        return restTemplate;
    }

    public static void main(String[] args) throws Exception {
        ServiceStartInit.preInitSystemConfig();
        ApplicationContext context = SpringApplication.run(TcpServiceApplicationStart.class, args);
        ServiceStartInit.initSystemConfig(context);

        //服务启动完成
        ServiceStartInit.printStartSuccessInfo();

        // 启动门控、梯控tcp服务
//        TCPServer tcpServer = new TCPServer();
//        tcpServer.run();
    }
}