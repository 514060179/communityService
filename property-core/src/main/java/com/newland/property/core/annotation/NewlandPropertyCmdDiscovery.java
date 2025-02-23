package com.newland.property.core.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 侦听注入
 * Created by wuxw on 2018/7/2.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(NewlandPropertyCmdDiscoveryRegistrar.class)
public @interface NewlandPropertyCmdDiscovery {

    String[] basePackages() default {};

    String[] value() default {};

    Class<?> cmdPublishClass();
}
