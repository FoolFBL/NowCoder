package com.kong.newcoder.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author shijiu
// */
//@Component
//@Aspect
public class AlphaAspect {
    @Pointcut("execution(* com.kong.newcoder.Service.*.*(..))")
    public void pointcut(){


    }

    @Before("pointcut()")
    public void before(){

        System.out.println("before");
    }

    @After("pointcut()")
    public void after(){
        System.out.println("after");
    }

    @AfterReturning("pointcut()")
    public void AfterReturning(){
        System.out.println("AfterReturning");
    }
    @AfterThrowing("pointcut()")
    public void AfterThrowing(){
        System.out.println("AfterThrowing");
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("执行前");
        Object proceed = joinPoint.proceed();
        System.out.println("执行后");
        return proceed;
    }

}
