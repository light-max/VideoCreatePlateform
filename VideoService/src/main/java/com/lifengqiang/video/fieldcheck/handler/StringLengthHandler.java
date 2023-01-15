package com.lifengqiang.video.fieldcheck.handler;

import com.lifengqiang.video.fieldcheck.FieldCheckException;
import com.lifengqiang.video.fieldcheck.annotation.StringLengthMax;
import com.lifengqiang.video.fieldcheck.annotation.StringLengthMin;

import java.lang.annotation.Annotation;

/**
 * 字符串长度处理程序
 */
public class StringLengthHandler implements BaseHandler {
    @Override
    public void process(Object field, Annotation rule) throws FieldCheckException {
        String string = String.valueOf(field);
        Class<? extends Annotation> annotationType = rule.annotationType();
        if (annotationType.equals(StringLengthMax.class)) {
            StringLengthMax a = (StringLengthMax) rule;
            if (field == null && a.isNull()) {
                return;
            }
            string = a.trim() ? string.trim() : string;
            if (string.length() > a.value()) {
                throw new FieldCheckException(a.msg().replace("${value}", String.valueOf(a.value())));
            }
        } else if (annotationType.equals(StringLengthMin.class)) {
            StringLengthMin a = (StringLengthMin) rule;
            if (field == null && a.isNull()) {
                return;
            }
            string = a.trim() ? string.trim() : string;
            if (string.length() < a.value()) {
                throw new FieldCheckException(a.msg().replace("${value}", String.valueOf(a.value())));
            }
        }
    }
}
