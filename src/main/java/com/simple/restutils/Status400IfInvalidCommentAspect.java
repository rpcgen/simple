package com.simple.restutils;

import com.simple.service.SimpleCommentRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;

@Aspect
@Component
public class Status400IfInvalidCommentAspect {

    private SimpleCommentRepository repository;

    @Autowired
    public void setSimpleCommentRepository(SimpleCommentRepository repository) {
        this.repository = repository;
    }

    @Pointcut("@annotation(com.simple.restutils.Status400IfInvalidComment)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        boolean anyNonExistingEntity = findInArgs(pjp.getArgs(), Long.class)
                .stream()
                .anyMatch($ -> !repository.exists($));

        if (anyNonExistingEntity)
            findfirstInArgs(pjp.getArgs(), HttpServletResponse.class).setStatus(SC_BAD_REQUEST);

        return pjp.proceed();
    }

    private <T> List<T> findInArgs(Object[] args, Class<T> clazz) {
        return asList(args).stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(toList());
    }

    private <T> T findfirstInArgs(Object[] args, Class<T> clazz) {
        return asList(args).stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .get();
    }
}
