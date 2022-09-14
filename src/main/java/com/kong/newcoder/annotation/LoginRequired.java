package com.kong.newcoder.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shijiu
 * 注解 是否需要登录
 */
//注解用来描述方法
@Target(ElementType.METHOD)
//程序运行时有效
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired
{

}
