package com.newland.property.doc.annotation;

import com.newland.property.doc.registrar.NewlandPropertyCmdDocDiscoveryRegistrar;
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
@Import(NewlandPropertyCmdDocDiscoveryRegistrar.class)
public @interface NewlandPropertyCmdDocDiscovery {

    String[] basePackages() default {};

    String[] value() default {};

    Class<?> cmdDocClass();
}
