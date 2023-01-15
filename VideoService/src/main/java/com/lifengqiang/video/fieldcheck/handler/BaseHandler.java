package com.lifengqiang.video.fieldcheck.handler;

import com.lifengqiang.video.fieldcheck.FieldCheckException;

import java.lang.annotation.Annotation;

/**
 * 处理程序的接口
 *
 * @author 李凤强
 */
public interface BaseHandler {
    /**
     * 所有的处理程序都要实现这个接口
     *
     * @param field 字段实体 不为空
     * @param rule  处理规则 不为空
     * @throws FieldCheckException 如果字段内容不符合规则就会抛出异常
     */
    void process(Object field, Annotation rule) throws FieldCheckException;
}
