package com.lifengqiang.video.util.datetranslate;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface DateTranslate {

    /**
     * 把类中标识了{@link DateParameter}注解的字段转换成对应{@link DateParameter#value()}的格式
     *
     * @return 格式化后的字符串日期
     * @throws RuntimeException 如果没有找到这个类中有{@link DateParameter}注解的字段就会抛出异常
     */
    default String translateDate() {
        List<Field> fields = new ArrayList<>();
        for (Class<?> aClass = getClass();
             !aClass.equals(Object.class);
             aClass = aClass.getSuperclass()) {
            fields.addAll(Arrays.asList(aClass.getDeclaredFields()));
        }
        for (Field field : fields) {
            DateParameter dateParameter = field.getAnnotation(DateParameter.class);
            if (dateParameter != null) {
                try {
                    field.setAccessible(true);
                    Object o = field.get(this);
                    return new SimpleDateFormat(dateParameter.value()).format(o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new RuntimeException("没有找到有: " + DateParameter.class + "注解的字段");
    }
}
