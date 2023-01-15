package com.lifengqiang.video.fieldcheck.handler;

import com.lifengqiang.video.fieldcheck.FieldCheckException;
import com.lifengqiang.video.fieldcheck.annotation.StringEnum;

import java.lang.annotation.Annotation;

public class StringEnumHandler implements BaseHandler {
    @Override
    public void process(Object field, Annotation rule) throws FieldCheckException {
        StringEnum a = (StringEnum) rule;
        if (field == null && a.isNull()) {
            return;
        }
        String string = String.valueOf(field);
        boolean error = true;
        for (String s : a.value()) {
            if (string.equals(s)) {
                error = false;
                break;
            }
        }
        if (error) {
            throw new FieldCheckException(a.msg());
        }
    }
}
