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
package com.newland.property.boot;

import com.newland.property.core.annotation.NewlandPropertyCmdDiscovery;
import com.newland.property.core.client.OutRestTemplate;
import com.newland.property.core.client.RestTemplate;
import com.newland.property.core.context.Environment;
import com.newland.property.core.event.cmd.ServiceCmdEventPublishing;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.core.trace.NewlandPropertyFeignClientInterceptor;
import com.newland.property.core.trace.NewlandPropertyRestTemplateInterceptor;
import com.newland.property.doc.annotation.NewlandPropertyApiDocDiscovery;
import com.newland.property.doc.annotation.NewlandPropertyCmdDocDiscovery;
import com.newland.property.doc.registrar.ApiDocCmdPublishing;
import com.newland.property.doc.registrar.ApiDocPublishing;
import com.newland.property.intf.dev.ICacheV1InnerServiceSMO;
import com.newland.property.service.init.ServiceStartInit;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.StringUtil;
import okhttp3.ConnectionPool;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;


/**
 * 这个服务是将 系统部署为spring boot版
 * 如果是spring cloud 微服务部署 不用启动这个类
 * <p>
 *
 * @version v0.1
 * @auther com.newland.property.wuxw
 * @mail 928255095@qq.com
 * @date 2016年8月6日
 * @tag
 */
@SpringBootApplication(scanBasePackages = {
        "com.newland.property.service",
        "com.newland.property.db",
        "com.newland.property.core",
        "com.newland.property.config.properties.code",
        "com.newland.property.acct",
        "com.newland.property.common",
        "com.newland.property.community",
        "com.newland.property.dev",
        "com.newland.property.fee",
        "com.newland.property.job",
        "com.newland.property.oa",
        "com.newland.property.tcp",
        "com.newland.property.order",
        "com.newland.property.report",
        "com.newland.property.store",
        "com.newland.property.user",
        "com.newland.property.doc",
        "com.newland.property.scm",
        "com.newland.property.boot"
},
        exclude = {LiquibaseAutoConfiguration.class,
                org.activiti.spring.boot.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                com.github.pagehelper.autoconfigure.MapperAutoConfiguration.class
        }

)
@NewlandPropertyCmdDiscovery(cmdPublishClass = ServiceCmdEventPublishing.class,
        basePackages = {
                "com.newland.property.acct.cmd",
                "com.newland.property.common.cmd",
                "com.newland.property.community.cmd",
                "com.newland.property.dev.cmd",
                "com.newland.property.fee.cmd",
                "com.newland.property.job.cmd",
                "com.newland.property.oa.cmd",
                "com.newland.property.tcp.cmd",
                "com.newland.property.order.cmd",
                "com.newland.property.report.cmd",
                "com.newland.property.store.cmd",
                "com.newland.property.scm.cmd",
                "com.newland.property.user.cmd"
        })
@EnableScheduling
@EnableAsync
//文档
@NewlandPropertyApiDocDiscovery(basePackages = {"com.newland.property.boot.rest"}, apiDocClass = ApiDocPublishing.class)
@NewlandPropertyCmdDocDiscovery(basePackages = {
        "com.newland.property.acct.cmd",
        "com.newland.property.acct.payment.business",
        "com.newland.property.common.cmd",
        "com.newland.property.community.cmd",
        "com.newland.property.dev.cmd",
        "com.newland.property.fee.cmd",
        "com.newland.property.job.cmd",
        "com.newland.property.oa.cmd",
        "com.newland.property.tcp.cmd",
        "com.newland.property.order.cmd",
        "com.newland.property.report.cmd",
        "com.newland.property.store.cmd",
        "com.newland.property.scm.cmd",
        "com.newland.property.user.cmd"
},
        cmdDocClass = ApiDocCmdPublishing.class)
public class BootApplicationStart {

    private static Logger logger = LoggerFactory.getLogger(BootApplicationStart.class);

    @Resource
    private NewlandPropertyRestTemplateInterceptor newlandPropertyRestTemplateInterceptor;

    /**
     * 实例化RestTemplate
     *
     * @return restTemplate
     */
    @Bean
    public OutRestTemplate outRestTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        m.setWriteAcceptCharset(false);
        OutRestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build(OutRestTemplate.class);
        restTemplate.getInterceptors().add(newlandPropertyRestTemplateInterceptor);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //重试次数
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
        //最大连接数
        httpClientBuilder.setMaxConnTotal(1000);
        //每个路由最大链接数量
        httpClientBuilder.setMaxConnPerRoute(1000);
        CloseableHttpClient build = httpClientBuilder.build();
        //设置超时时间
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(build);
        httpRequestFactory.setConnectionRequestTimeout(10000);
        httpRequestFactory.setConnectTimeout(10000);
        httpRequestFactory.setReadTimeout(10000);
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
        m.setWriteAcceptCharset(false);
        RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build(RestTemplate.class);
        restTemplate.getInterceptors().add(newlandPropertyRestTemplateInterceptor);
        //设置超时时间
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(10000);
        httpRequestFactory.setConnectTimeout(10000);
        httpRequestFactory.setReadTimeout(10000);
        restTemplate.setRequestFactory(httpRequestFactory);
        return restTemplate;
    }

    @Bean
    @ConditionalOnBean(NewlandPropertyFeignClientInterceptor.class)
    public okhttp3.OkHttpClient okHttpClient(@Autowired
                                             NewlandPropertyFeignClientInterceptor okHttpLoggingInterceptor) {
        okhttp3.OkHttpClient.Builder ClientBuilder = new okhttp3.OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS) //读取超时
                .connectTimeout(10, TimeUnit.SECONDS) //连接超时
                .writeTimeout(60, TimeUnit.SECONDS) //写入超时
                .connectionPool(new ConnectionPool(10 /*maxIdleConnections*/, 3, TimeUnit.MINUTES))
                .addInterceptor(okHttpLoggingInterceptor);
        return ClientBuilder.build();
    }

    public static void main(String[] args) throws Exception {
        try {
            ServiceStartInit.preInitSystemConfig();
            ApplicationContext context = SpringApplication.run(BootApplicationStart.class, args);
            //服务启动加载
            ServiceStartInit.initSystemConfig(context);

            Environment.setSystemStartWay(Environment.SPRING_BOOT);

            //刷新缓存
            flushMainCache(args);

            //服务启动完成
            ServiceStartInit.printStartSuccessInfo();

            // 启动门控、梯控tcp服务
//            TCPServer tcpServer = new TCPServer();
//            tcpServer.run();
        } catch (Throwable e) {
            logger.error("系统启动失败", e);
        }
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
        String mapping = "";
        if (StringUtil.isEmpty(mapping)) {
            ICacheV1InnerServiceSMO devServiceCacheSMOImpl = (ICacheV1InnerServiceSMO) ApplicationContextFactory.getBean(ICacheV1InnerServiceSMO.class);
            devServiceCacheSMOImpl.startFlush();
            return;
        }

        if (args == null || args.length == 0) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if ("-Dcache".equalsIgnoreCase(args[i])) {
                logger.debug("开始刷新日志，入参为：{}", args[i]);
                ICacheV1InnerServiceSMO devServiceCacheSMOImpl = (ICacheV1InnerServiceSMO) ApplicationContextFactory.getBean(ICacheV1InnerServiceSMO.class);
                devServiceCacheSMOImpl.startFlush();
            }
        }
    }

}