package dev.gsitgithub.webapp.config.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@Slf4j
@Aspect
public class AopLoggingConfig {

    @Before(
    value = "execution(public * dev.gsitgithub.webapp..controller..*.*(..))",
    argNames = "joinPoint")
    public void before(JoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String arguments = Arrays.asList(joinPoint.getArgs()).size() > 0 ? Arrays.asList(joinPoint.getArgs()).toString(): "";
        log.trace(className + "." + methodName + "(" + arguments + ")");
    }

    @AfterReturning(
    pointcut = "execution(public * dev.gsitgithub.webapp..controller..*.*(..))",
    returning = "returnValue")
    public void afterReturning(Object returnValue) throws Throwable {
        log.trace("return " + returnValue);
    }
}
