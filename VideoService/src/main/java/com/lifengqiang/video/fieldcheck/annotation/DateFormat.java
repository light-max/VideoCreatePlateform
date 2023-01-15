package com.lifengqiang.video.fieldcheck.annotation;

import com.lifengqiang.video.fieldcheck.Handler;
import com.lifengqiang.video.fieldcheck.handler.DateFormatHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日期格式, 被注解的字符串格式必须符合{@link #value()}的格式
 *
 * @author 李凤强
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Handler(DateFormatHandler.class)
public @interface DateFormat {
    /**
     * 错误信息
     */
    String msg() default "";

    String value() default DEFAULT;

    String DEFAULT = "yyyy/MM/dd";

    boolean isNull() default true;
}
