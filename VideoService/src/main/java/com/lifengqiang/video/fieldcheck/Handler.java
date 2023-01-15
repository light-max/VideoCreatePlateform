package com.lifengqiang.video.fieldcheck;

import com.lifengqiang.video.fieldcheck.handler.BaseHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定字段处理程序的注解
 *
 * @author 李凤强
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Handler {
    /**
     * 处理程序的类，这个类必须实现{@link BaseHandler}接口
     */
    Class<? extends BaseHandler> value();
}
