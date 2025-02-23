package com.newland.property.report;

import com.newland.property.core.annotation.NewlandPropertyCmdDiscovery;
import com.newland.property.core.trace.NewlandPropertyRestTemplateInterceptor;
import com.newland.property.core.client.RestTemplate;
import com.newland.property.core.event.cmd.ServiceCmdEventPublishing;
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
@SpringBootApplication(scanBasePackages = {"com.newland.property.service", "com.newland.property.report",
        "com.newland.property.core", "com.newland.property.config.properties.code", "com.newland.property.db","com.newland.property.doc"},
        exclude = {LiquibaseAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@EnableDiscoveryClient
@NewlandPropertyCmdDiscovery(cmdPublishClass = ServiceCmdEventPublishing.class,
        basePackages = {"com.newland.property.report.cmd"})
@EnableFeignClients(basePackages = {"com.newland.property.intf.user",
        "com.newland.property.intf.order",
        "com.newland.property.intf.common",
        "com.newland.property.intf.store",
        "com.newland.property.intf.user",
        "com.newland.property.intf.fee",
        "com.newland.property.intf.dev",
        "com.newland.property.intf.community"})
public class ReportServiceApplicationStart {

    private static Logger logger = LoggerFactory.getLogger(ReportServiceApplicationStart.class);

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

        ApplicationContext context = SpringApplication.run(ReportServiceApplicationStart.class, args);
        ServiceStartInit.initSystemConfig(context);

        //初始化 activity 流程
        //DeploymentActivity.deploymentProcess();

        //服务启动完成
        ServiceStartInit.printStartSuccessInfo();
    }
}