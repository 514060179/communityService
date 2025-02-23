package com.newland.property.doc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewlandPropertyParamDoc {

    String parentNodeName() default "-";

    String name();

    String type() default "String";

    int length() default 0;

    String defaultValue() default "";

    String remark() default "";
}
