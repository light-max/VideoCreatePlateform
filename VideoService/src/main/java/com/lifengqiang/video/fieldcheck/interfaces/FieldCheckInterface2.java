package com.lifengqiang.video.fieldcheck.interfaces;

import com.lifengqiang.video.fieldcheck.CheckUtils;
import com.lifengqiang.video.fieldcheck.FieldCheckException;

public interface FieldCheckInterface2 {
    /**
     * 进行字段检测
     *
     * @return 返回子类
     * @throws FieldCheckException 当对象中标有检测注解的字段没有达到条件时就会抛出异常
     */
    default <T> T check() throws FieldCheckException {
        return (T) CheckUtils.check(this);
    }
}
