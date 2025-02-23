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
package com.newland.property.api;

import com.newland.property.core.annotation.NewlandPropertyListenerDiscovery;
import com.newland.property.core.client.OutRestTemplate;
import com.newland.property.core.trace.NewlandPropertyFeignClientInterceptor;
import com.newland.property.core.trace.NewlandPropertyRestTemplateInterceptor;
import com.newland.property.core.client.RestTemplate;
import com.newland.property.core.event.service.api.ServiceDataFlowEventPublishing;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.doc.annotation.NewlandPropertyApiDocDiscovery;
import com.newland.property.doc.registrar.ApiDocPublishing;
import com.newland.property.service.init.ServiceStartInit;
import io.swagger.annotations.ApiOperation;
import okhttp3.ConnectionPool;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;


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
        "com.newland.property.service.configuration",
        "com.newland.property.service.init",
        "com.newland.property.api",
        "com.newland.property.core",
        "com.newland.property.config.properties.code",
        "com.newland.property.doc",
})
@EnableDiscoveryClient
@NewlandPropertyListenerDiscovery(listenerPublishClass = ServiceDataFlowEventPublishing.class,
        basePackages = {"com.newland.property.api.listener"})
@EnableSwagger2
//@EnableConfigurationProperties(EventProperties.class)
@EnableFeignClients(basePackages = {
        "com.newland.property.intf.acct",
        "com.newland.property.intf.code",
        "com.newland.property.intf.common",
        "com.newland.property.intf.community",
        "com.newland.property.intf.demo",
        "com.newland.property.intf.dev",
        "com.newland.property.intf.fee",
        "com.newland.property.intf.goods",
        "com.newland.property.intf.job",
        "com.newland.property.intf.oa",
        "com.newland.property.intf.order",
        "com.newland.property.intf.report",
        "com.newland.property.intf.store",
        "com.newland.property.intf.user"
})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableAsync

//文档
@NewlandPropertyApiDocDiscovery(basePackages = {"com.newland.property.api.rest"},apiDocClass = ApiDocPublishing.class)
public class ApiApplicationStart {

    private static Logger logger = LoggerFactory.getLogger(ApiApplicationStart.class);

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
        //设置超时时间
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(10000);
        httpRequestFactory.setConnectTimeout(10000);
        httpRequestFactory.setReadTimeout(10000);
        restTemplate.setRequestFactory(httpRequestFactory);
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

    /**
     * swagger 插件
     *
     * @return Docket 对象
     */
    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).build();
    }


    @Bean
    @ConditionalOnBean(NewlandPropertyFeignClientInterceptor.class)
    public okhttp3.OkHttpClient okHttpClient(@Autowired
                                             NewlandPropertyFeignClientInterceptor okHttpLoggingInterceptor){
        okhttp3.OkHttpClient.Builder ClientBuilder = new okhttp3.OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS) //读取超时
                .connectTimeout(10, TimeUnit.SECONDS) //连接超时
                .writeTimeout(60, TimeUnit.SECONDS) //写入超时
                .connectionPool(new ConnectionPool(10 /*maxIdleConnections*/, 3, TimeUnit.MINUTES))
                .addInterceptor(okHttpLoggingInterceptor);
        return ClientBuilder.build();
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("HC小区管理系统 APIs")
                .description("HC小区管理系统提供的所有能力")
                .termsOfServiceUrl("https://ezone.work/newlandwb/newland-property/NewlandProperty.git")
                .contact("吴学文")
                .version("1.0")
                .build();
    }


    public static void main(String[] args) throws Exception {
        try {
            ServiceStartInit.preInitSystemConfig();
            ApplicationContext context = SpringApplication.run(ApiApplicationStart.class, args);
            //服务启动加载
            ServiceStartInit.initSystemConfig(context);
            //服务启动完成
            ServiceStartInit.printStartSuccessInfo();
        } catch (Throwable e) {
            logger.error("系统启动失败", e);
        }
    }

}