package com.newland.property.job;

import com.newland.property.core.annotation.NewlandPropertyCmdDiscovery;
import com.newland.property.core.annotation.NewlandPropertyListenerDiscovery;
import com.newland.property.core.client.OutRestTemplate;
import com.newland.property.core.client.RestTemplate;
import com.newland.property.core.event.cmd.ServiceCmdEventPublishing;
import com.newland.property.core.event.service.BusinessServiceDataFlowEventPublishing;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.core.trace.NewlandPropertyRestTemplateInterceptor;
import com.newland.property.service.init.ServiceStartInit;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.nio.charset.Charset;

@SpringBootApplication(
        scanBasePackages = {"com.newland.property.service",
                "com.newland.property.job",
                "com.newland.property.core",
                "com.newland.property.config.properties.code",
                "com.newland.property.db",
                "com.newland.property.doc"},
        excludeName = {"com.newland.property.intf.jobservice"}
)
@EnableDiscoveryClient
@NewlandPropertyListenerDiscovery(listenerPublishClass = BusinessServiceDataFlowEventPublishing.class,
        basePackages = {"com.newland.property.job.listener"})
@NewlandPropertyCmdDiscovery(cmdPublishClass = ServiceCmdEventPublishing.class,
        basePackages = {"com.newland.property.job.cmd"})
@EnableFeignClients(basePackages = {
        "com.newland.property.intf.community",
        "com.newland.property.intf.common",
        "com.newland.property.intf.fee",
        "com.newland.property.intf.user",
        "com.newland.property.intf.order",
        "com.newland.property.intf.store",
        "com.newland.property.intf.report",
        "com.newland.property.intf.acct",
        "com.newland.property.intf.oa",
        "com.newland.property.intf.dev",
        "com.newland.property.intf.goods"
})
@EnableScheduling
@EnableAsync
public class JobServiceApplication {
    private static Logger logger = LoggerFactory.getLogger(JobServiceApplication.class);

    private static final String LISTENER_PATH = "newland.job.listeners";

    @Resource
    private NewlandPropertyRestTemplateInterceptor newlandPropertyRestTemplateInterceptor;

    @Bean
    public OutRestTemplate outRestTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        OutRestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build(OutRestTemplate.class);
        restTemplate.getInterceptors().add(newlandPropertyRestTemplateInterceptor);

        //设置超时时间
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(5000);
        httpRequestFactory.setConnectTimeout(5000);
        httpRequestFactory.setReadTimeout(5000);
        restTemplate.setRequestFactory(httpRequestFactory);
        return restTemplate;
    }

    @Bean
    //@LoadBalanced
    public RestTemplate formRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

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
        return restTemplate;
    }

    public static void main(String[] args) throws Exception {
        ServiceStartInit.preInitSystemConfig();
        ApplicationContext context = SpringApplication.run(JobServiceApplication.class, args);
        ServiceStartInit.initSystemConfig(context);
        //加载业务侦听
        // SystemStartLoadBusinessConfigure.initSystemConfig(LISTENER_PATH);

        //服务启动完成
        ServiceStartInit.printStartSuccessInfo();


    }
}
