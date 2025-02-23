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
package com.newland.property.common;

import com.newland.property.core.annotation.NewlandPropertyCmdDiscovery;
import com.newland.property.core.annotation.NewlandPropertyListenerDiscovery;
import com.newland.property.core.trace.NewlandPropertyRestTemplateInterceptor;
import com.newland.property.core.client.RestTemplate;
import com.newland.property.core.event.cmd.ServiceCmdEventPublishing;
import com.newland.property.core.event.service.BusinessServiceDataFlowEventPublishing;
import com.newland.property.service.init.ServiceStartInit;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
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
 * @auther com.newland.property.wuxw
 * @mail 928255095@qq.com
 * @date 2016年8月6日
 * @tag
 */
@SpringBootApplication(scanBasePackages = {
        "com.newland.property.service",
        "com.newland.property.common",
        "com.newland.property.core",
        "com.newland.property.config.properties.code",
        "com.newland.property.db",
        "com.newland.property.doc"},
        exclude = {LiquibaseAutoConfiguration.class,
                org.activiti.spring.boot.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class}
        )
@EnableDiscoveryClient
@NewlandPropertyListenerDiscovery(listenerPublishClass = BusinessServiceDataFlowEventPublishing.class,
        basePackages = {"com.newland.property.common.listener"})
@NewlandPropertyCmdDiscovery(cmdPublishClass = ServiceCmdEventPublishing.class,
        basePackages = {"com.newland.property.common.cmd"})
@EnableFeignClients(basePackages = {
        "com.newland.property.intf.user",
        "com.newland.property.intf.store",
        "com.newland.property.intf.fee",
        "com.newland.property.intf.community",
        "com.newland.property.intf.job",
        "com.newland.property.intf.order",
        "com.newland.property.intf.oa",
        "com.newland.property.intf.report",
        "com.newland.property.intf.acct",
        "com.newland.property.intf.api"
})
public class CommonServiceApplicationStart {

    private static Logger logger = LoggerFactory.getLogger(CommonServiceApplicationStart.class);

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
        ApplicationContext context = SpringApplication.run(CommonServiceApplicationStart.class, args);
        ServiceStartInit.initSystemConfig(context);

        //初始化 activity 流程
        //DeploymentActivity.deploymentProcess();
        //服务启动完成
        ServiceStartInit.printStartSuccessInfo();
    }
}