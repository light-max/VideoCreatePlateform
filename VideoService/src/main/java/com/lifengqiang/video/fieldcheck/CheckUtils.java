package com.lifengqiang.video.fieldcheck;

import com.lifengqiang.video.fieldcheck.handler.BaseHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CheckUtils {
    /**
     * 检测程序对象集合, key:程序类名 ,value:程序对象
     */
    private final static Map<Class<? extends BaseHandler>, BaseHandler> handlers = new HashMap<>();

    /**
     * 调用检测程序检测对象中的字段
     *
     * @param pojo 待检测的对象
     * @return 返回对象本身
     * @throws RuntimeException    代码bug
     * @throws FieldCheckException 字段异常
     */
    public static Object check(Object pojo) throws RuntimeException {
        try {
            for (Field field : pojo.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object o = field.get(pojo);
                if (o == null) continue;
                for (Annotation annotation : field.getAnnotations()) {
                    BaseHandler handler = getHandler(annotation);
                    if (handler != null) {
                        handler.process(o, annotation);
                    }
                }
            }
            return pojo;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据检测条件获取检测程序
     *
     * @param annotation 标有{@link Handler}注解的检测条件注解
     * @return 检测条件所指定的处理程序, 入参注解没有标注检测程序则返回null
     */
    private static BaseHandler getHandler(Annotation annotation) {
        Handler handler = annotation.annotationType().getAnnotation(Handler.class);
        if (handler != null) {
            Class<? extends BaseHandler> handlerClass = handler.value();
            BaseHandler baseHandler = handlers.get(handlerClass);
            if (baseHandler == null) {
                try {
                    Constructor<? extends BaseHandler> constructor = handlerClass.getConstructor();
                    baseHandler = constructor.newInstance();
                    handlers.put(handlerClass, baseHandler);
                } catch (NoSuchMethodException e) {
                    System.err.println("无法访问目标类的构造函数: " + handlerClass.toString());
                    throw new RuntimeException(e);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    System.err.println("无法构造处理程序: " + handlerClass.toString());
                    throw new RuntimeException(e);
                }
            }
            return baseHandler;
        }
        return null;
    }
}
