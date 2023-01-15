package com.lifengqiang.video.fieldcheck.annotation;

import com.lifengqiang.video.fieldcheck.Handler;
import com.lifengqiang.video.fieldcheck.handler.NumberSizeHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数字的最小值
 * 只支持{@link Integer}类型, 和{@link Long}类型
 *
 * @author 李凤强
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Handler(NumberSizeHandler.class)
public @interface NumberMin {
    /**
     * 错误信息
     * 可以使用${value}将{@link #value()}输出到错误信息中
     */
    String msg() default "";

    long value();
}
