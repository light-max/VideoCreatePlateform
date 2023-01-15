package com.lifengqiang.video.fieldcheck.annotation;

import com.lifengqiang.video.fieldcheck.Handler;
import com.lifengqiang.video.fieldcheck.handler.NumberSizeHandler;
import com.lifengqiang.video.fieldcheck.handler.StringEnumHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Handler(StringEnumHandler.class)
public @interface StringEnum {
    String[] value();

    String msg() default "";

    boolean isNull() default true;
}
