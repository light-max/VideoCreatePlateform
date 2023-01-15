package com.lifengqiang.video.fieldcheck.annotation;

import com.lifengqiang.video.fieldcheck.Handler;
import com.lifengqiang.video.fieldcheck.handler.StringRegexHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 正则表达式
 *
 * @author 李凤强
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Handler(StringRegexHandler.class)
public @interface StringRegex {
    /**
     * 错误信息
     */
    String msg() default "";

    String value();
}
