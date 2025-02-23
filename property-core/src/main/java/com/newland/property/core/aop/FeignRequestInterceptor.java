package com.newland.property.core.aop;

import com.newland.property.core.factory.PropertyTransactionalFactory;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @ClassName FeignRequestInterceptor
 * @Description TODO feign 调用拦截器
 * @Author wuxw
 * @Date 2020/7/5 22:44
 * @Version 1.0
 * add by wuxw 2020/7/5
 **/
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    /**
     * 传入事务ID
     *
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String oId = PropertyTransactionalFactory.getOId();
        if (!StringUtils.isEmpty(oId)) {
            requestTemplate.header(PropertyTransactionalFactory.O_ID, oId);
        }
    }
}
