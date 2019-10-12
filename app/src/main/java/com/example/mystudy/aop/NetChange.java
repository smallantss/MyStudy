package com.example.mystudy.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//网络变化的时候调用
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NetChange {
    NetType netType() default NetType.NET_ALL;
}
