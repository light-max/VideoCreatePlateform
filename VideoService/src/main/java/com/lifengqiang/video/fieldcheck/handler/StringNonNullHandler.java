package com.lifengqiang.video.fieldcheck.handler;

import com.lifengqiang.video.fieldcheck.FieldCheckException;
import com.lifengqiang.video.fieldcheck.annotation.StringNonNull;

import java.lang.annotation.Annotation;

/**
 * 字符串不为空的注解处理程序
 *
 * @author 李凤强
 */
public class StringNonNullHandler implements BaseHandler {
    @Override
    public void process(Object field, Annotation rule) throws FieldCheckException {
        StringNonNull a = (StringNonNull) rule;
        if (field == null && a.isNull()) {
            return;
        }
        String string = String.valueOf(field);
        string = a.trim() ? string.trim() : string;
        if (string.isEmpty()) {
            throw new FieldCheckException(a.value());
        }
    }
}
