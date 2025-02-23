package com.newland.property.dev;

import com.newland.property.core.annotation.NewlandPropertyCmdDiscovery;
import com.newland.property.core.trace.NewlandPropertyRestTemplateInterceptor;
import com.newland.property.core.client.RestTemplate;
import com.newland.property.core.event.cmd.ServiceCmdEventPublishing;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dev.smo.IDevServiceCacheSMO;
import com.newland.property.service.init.ServiceStartInit;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.StringUtil;
import org.slf4j.Logger;
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
@SpringBootApplication(scanBasePackages = {"com.newland.property.service", "com.newland.property.dev",
        "com.newland.property.core", "com.newland.property.config.properties.code", "com.newland.property.db", "com.newland.property.utils.factory","com.newland.property.doc"},
        exclude = {LiquibaseAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@EnableDiscoveryClient
@NewlandPropertyCmdDiscovery(cmdPublishClass = ServiceCmdEventPublishing.class,
        basePackages = {"com.newland.property.dev.cmd"})
@EnableFeignClients(basePackages = {"com.newland.property.intf.user",
        "com.newland.property.intf.order",
        "com.newland.property.intf.common",
        "com.newland.property.intf.community",
        "com.newland.property.intf.store",
        "com.newland.property.intf.job"})
public class DevServiceApplicationStart {

    private static Logger logger = LoggerFactory.getLogger(DevServiceApplicationStart.class);

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
        ApplicationContext context = SpringApplication.run(DevServiceApplicationStart.class, args);
        ServiceStartInit.initSystemConfig(context);

        //刷新缓存
        flushMainCache(args);
//
//        //所有的bean,参考：http://412887952-qq-com.iteye.com/blog/2314051
//        String[] beanNames = context.getBeanDefinitionNames();
//        //String[] beanNames = ctx.getBeanNamesForAnnotation(RestController.class);//所有添加该注解的bean
//        logger.info("bean总数:{}", context.getBeanDefinitionCount());
//        int i = 0;
//        for (String str : beanNames) {
//            logger.info("{},beanName:{}", ++i, str);
//        }

        //服务启动完成
        ServiceStartInit.printStartSuccessInfo();

    }

    /**
     * 刷新主要的缓存
     *
     * @param args
     */
    private static void flushMainCache(String[] args) {

        logger.debug("判断是否需要刷新日志，参数 args 为 {}", args);

        //因为好多朋友启动时 不加 参数-Dcache 所以启动时检测 redis 中是否存在 newland_property_version
        //String mapping = MappingCache.getValue(MappingConstant.ENV_DOMAIN,"newland_property_version");
        //这里改成强制刷新 因为好多 小伙伴 教不会 如何清理redis 干脆后面就说重启dev 就会刷新缓存 二开的兄弟们根据自己的情况 是否强制
        String mapping = "";
        if (StringUtil.isEmpty(mapping)) {
            IDevServiceCacheSMO devServiceCacheSMOImpl = (IDevServiceCacheSMO) ApplicationContextFactory.getBean("devServiceCacheSMOImpl");
            devServiceCacheSMOImpl.startFlush();
            return;
        }

        if (args == null || args.length == 0) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if ("-Dcache".equalsIgnoreCase(args[i])) {
                logger.debug("开始刷新日志，入参为：{}", args[i]);
                IDevServiceCacheSMO devServiceCacheSMOImpl = (IDevServiceCacheSMO) ApplicationContextFactory.getBean("devServiceCacheSMOImpl");
                devServiceCacheSMOImpl.startFlush();
            }
        }
    }
}