package com.simple.restutils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;


import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;

@Aspect
@Component
public class Status204IfEmptyCollectionAspect {

    @Pointcut("@annotation(com.simple.restutils.Status204IfEmptyCollection)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        Object result = pjp.proceed();

        if (result instanceof Collection) {
            if (Collection.class.cast(result).isEmpty()) {
                HttpServletResponse response = findInArgs(pjp.getArgs(), HttpServletResponse.class);
                response.setStatus(SC_NO_CONTENT);
            }
        }

        return result;
    }

    private <T> T findInArgs(Object[] args, Class<T> clazz) {

        for (Object object : args) {
            if (clazz.isInstance(object)) {
                return clazz.cast(object);
            }
        }

        return null;
    }
}
