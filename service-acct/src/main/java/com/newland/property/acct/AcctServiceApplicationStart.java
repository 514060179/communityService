package com.newland.property.acct;

import com.newland.property.core.annotation.NewlandPropertyCmdDiscovery;
import com.newland.property.core.annotation.NewlandPropertyListenerDiscovery;
import com.newland.property.core.client.OutRestTemplate;
import com.newland.property.core.trace.NewlandPropertyRestTemplateInterceptor;
import com.newland.property.core.client.RestTemplate;
import com.newland.property.core.event.cmd.ServiceCmdEventPublishing;
import com.newland.property.core.event.service.BusinessServiceDataFlowEventPublishing;
import com.newland.property.doc.annotation.NewlandPropertyCmdDocDiscovery;
import com.newland.property.doc.registrar.ApiDocCmdPublishing;
import com.newland.property.service.init.ServiceStartInit;
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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
@SpringBootApplication(
        scanBasePackages = {"com.newland.property.service","com.newland.property.core", "com.newland.property.acct",  "com.newland.property.config.properties.code", "com.newland.property.db","com.newland.property.doc"},
        excludeName = {"com.newland.property.intf.acct"}
)
@EnableDiscoveryClient
@NewlandPropertyListenerDiscovery(listenerPublishClass = BusinessServiceDataFlowEventPublishing.class,
        basePackages = {"com.newland.property.acct.listener"})
@NewlandPropertyCmdDiscovery(cmdPublishClass = ServiceCmdEventPublishing.class,
        basePackages = {"com.newland.property.acct.cmd"})
@EnableFeignClients(basePackages = {"com.newland.property.intf.community","com.newland.property.intf.common","com.newland.property.intf.store",
        "com.newland.property.intf.fee","com.newland.property.intf.order","com.newland.property.intf.user","com.newland.property.intf.scm","com.newland.property.intf.job"})

//文档
@NewlandPropertyCmdDocDiscovery(basePackages = {"com.newland.property.acct.cmd","com.newland.property.acct.payment.business"},cmdDocClass = ApiDocCmdPublishing.class)
public class AcctServiceApplicationStart {

    private static Logger logger = LoggerFactory.getLogger(AcctServiceApplicationStart.class);

    private static final String LISTENER_PATH = "newland.property.UserService.listeners";

    @Resource
    private NewlandPropertyRestTemplateInterceptor newlandPropertyRestTemplateInterceptor;

    public static void main(String[] args) throws Exception {
        try {
            ServiceStartInit.preInitSystemConfig();
            ApplicationContext context = SpringApplication.run(AcctServiceApplicationStart.class, args);
            ServiceStartInit.initSystemConfig(context);

            //加载业务侦听
            // SystemStartLoadBusinessConfigure.initSystemConfig(LISTENER_PATH);
            //服务启动完成
            ServiceStartInit.printStartSuccessInfo();
        } catch (Throwable e) {
            logger.error("系统启动失败", e);
        }
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
        restTemplate.getInterceptors().add(newlandPropertyRestTemplateInterceptor);
        return restTemplate;
    }

    /**
     * 实例化RestTemplate，通过@LoadBalanced注解开启均衡负载能力.
     *
     * @return restTemplate
     */
    @Bean
    public OutRestTemplate outRestTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        m.setWriteAcceptCharset(false);
        OutRestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build(OutRestTemplate.class);
        restTemplate.getInterceptors().add(newlandPropertyRestTemplateInterceptor);

        //设置超时时间
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(10000);
        httpRequestFactory.setConnectTimeout(10000);
        httpRequestFactory.setReadTimeout(10000);
        restTemplate.setRequestFactory(httpRequestFactory);
        return restTemplate;
    }
}