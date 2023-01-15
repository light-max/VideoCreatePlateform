package com.lifengqiang.video.fieldcheck.annotation;

import com.lifengqiang.video.fieldcheck.Handler;
import com.lifengqiang.video.fieldcheck.handler.StringLengthHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字符串长度最大值
 *
 * @author 李凤强
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Handler(StringLengthHandler.class)
public @interface StringLengthMax {
    /**
     * 错误信息<br>
     * 可以使用${value}将{@link #value()}输出到错误信息中
     */
    String msg() default "";

    /**
     * 是否调用{@link String#trim()}函数
     */
    boolean trim() default false;

    int value();

    boolean isNull() default true;
}
