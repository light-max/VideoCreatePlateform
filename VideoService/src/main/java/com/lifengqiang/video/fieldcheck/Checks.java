package com.lifengqiang.video.fieldcheck;

import com.lifengqiang.video.fieldcheck.interfaces.FieldCheckInterface2;

/**
 * 字段检测工具类
 *
 * @author 李凤强
 */
public class Checks {
    /**
     * 检测方法
     *
     * @param object 需要检测的类
     * @return 被检测的对象，返回对象本身，对象本身不会发送任何改变
     * @throws FieldCheckException 当对象中标有检测注解的字段没有达到条件时就会抛出异常
     */
    public static <T> T check(T object) throws FieldCheckException {
        if (object instanceof FieldCheckInterface2) {
            return ((FieldCheckInterface2) object).check();
        } else {
            if (object == null) {
                return null;
            }
            throw new RuntimeException(String.format(
                    "%s 没有继承 %s",
                    object.getClass().getName(),
                    FieldCheckInterface2.class.getName()
            ));
        }
    }
}
