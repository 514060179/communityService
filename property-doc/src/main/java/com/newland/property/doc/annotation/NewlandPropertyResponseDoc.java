package com.newland.property.doc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * request param
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewlandPropertyResponseDoc {


    NewlandPropertyHeaderDoc[] headers() default @NewlandPropertyHeaderDoc(name = "");

    NewlandPropertyParamDoc[] params() default @NewlandPropertyParamDoc(name = "");


}
