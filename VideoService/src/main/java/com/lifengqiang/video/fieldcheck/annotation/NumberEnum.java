package com.lifengqiang.video.fieldcheck.annotation;

import com.lifengqiang.video.fieldcheck.Handler;
import com.lifengqiang.video.fieldcheck.handler.NumberEnumHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数字枚举, 被注解的数字只能是{@link #value()}中的值
 *
 * @author 李凤强
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Handler(NumberEnumHandler.class)
public @interface NumberEnum {
    /**
     * 错误信息
     */
    String msg() default "";

    long[] value();
}
