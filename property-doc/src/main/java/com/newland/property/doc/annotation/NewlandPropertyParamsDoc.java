package com.newland.property.doc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewlandPropertyParamsDoc {

    NewlandPropertyHeaderDoc[] headers() default {
            @NewlandPropertyHeaderDoc(name="APP-ID",defaultValue = "通过dev账户分配应用",description = "应用APP-ID"),
            @NewlandPropertyHeaderDoc(name="TRANSACTION-ID",defaultValue = "uuid",description = "交易流水号"),
            @NewlandPropertyHeaderDoc(name="REQ-TIME",defaultValue = "20220917120915",description = "请求时间 YYYYMMDDhhmmss"),
            @NewlandPropertyHeaderDoc(name="property-LANG",defaultValue = "zh-cn",description = "语言中文"),
            @NewlandPropertyHeaderDoc(name="USER-ID",defaultValue = "-1",description = "调用用户ID 一般写-1"),
            @NewlandPropertyHeaderDoc(name="Authorization",defaultValue = "Bearer xxx",description = "除了登录接口以外，其他接口必传token ,例如 Bearer token"),
    };

    NewlandPropertyParamDoc[] params() default @NewlandPropertyParamDoc(name = "");


}
