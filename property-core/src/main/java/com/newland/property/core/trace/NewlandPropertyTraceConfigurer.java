package com.newland.property.core.trace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class NewlandPropertyTraceConfigurer extends WebMvcConfigurerAdapter {
    @Autowired
    private NewlandPropertyTraceHandlerInterceptor newlandPropertyTraceHandlerInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(newlandPropertyTraceHandlerInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

//    @Bean
//    @ConditionalOnBean(NewlandPropertyTraceConfigurer.class)
//    public okhttp3.OkHttpClient okHttpClient(@Autowired
//                                                     NewlandPropertyFeignClientInterceptor okHttpLoggingInterceptor){
//        okhttp3.OkHttpClient.Builder ClientBuilder = new okhttp3.OkHttpClient.Builder()
//                .readTimeout(30, TimeUnit.SECONDS) //读取超时
//                .connectTimeout(10, TimeUnit.SECONDS) //连接超时
//                .writeTimeout(60, TimeUnit.SECONDS) //写入超时
//                .connectionPool(new ConnectionPool(10 /*maxIdleConnections*/, 3, TimeUnit.MINUTES))
//                .addInterceptor(okHttpLoggingInterceptor);
//        return ClientBuilder.build();
//    }
}
