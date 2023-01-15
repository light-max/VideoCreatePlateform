package com.lifengqiang.video.config.aop;

import com.lifengqiang.video.util.ump.ViewModelParameter;
import com.lifengqiang.video.util.ump.ViewModelParameters;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * 把controller方法中的{@link ViewModelParameter}注解的值注入到{@link Model}
 */
@Aspect
@Component
public class ViewModelParameterAop {

    @Pointcut("@annotation(com.lifengqiang.video.util.ump.ViewModelParameter)")
    public void viewModelParameter() {
    }

    @Pointcut("@annotation(com.lifengqiang.video.util.ump.ViewModelParameters)")
    public void viewModelParameters() {
    }

    @Around("viewModelParameter()&&@annotation(parameter)")
    public Object parameter(ProceedingJoinPoint point, ViewModelParameter parameter) throws Throwable {
        for (Object arg : point.getArgs()) {
            if (arg instanceof Model) {
                ((Model) arg).addAttribute(parameter.key(), parameter.value());
                break;
            }
        }
        return point.proceed();
    }

    @Around("viewModelParameters()&&@annotation(parameters)")
    public Object parameters(ProceedingJoinPoint point, ViewModelParameters parameters) throws Throwable {
        for (Object arg : point.getArgs()) {
            if (arg instanceof Model) {
                Model model = (Model) arg;
                for (ViewModelParameter parameter : parameters.value()) {
                    model.addAttribute(parameter.key(), parameter.value());
                }
                break;
            }
        }
        return point.proceed();
    }
}
