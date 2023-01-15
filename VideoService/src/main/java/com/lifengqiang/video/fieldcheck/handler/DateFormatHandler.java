package com.lifengqiang.video.fieldcheck.handler;

import com.lifengqiang.video.fieldcheck.FieldCheckException;
import com.lifengqiang.video.fieldcheck.annotation.DateFormat;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 日期格式化检测程序
 */
public class DateFormatHandler implements BaseHandler {
    @Override
    public void process(Object field, Annotation rule) throws FieldCheckException {
        DateFormat a = (DateFormat) rule;
        if (field == null && a.isNull()) {
            return;
        }
        String s = String.valueOf(field);
        try {
            LocalDate.parse(s, DateTimeFormatter.ofPattern(a.value()));
        } catch (DateTimeParseException e) {
            throw new FieldCheckException(a.msg());
        }
    }
}
