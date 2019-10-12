package com.example.mystudy.aop;

import android.content.Context;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class TestAspect {

    //找到处理的切点
    //* *(..)  可以处理CheckNet这个类所有的方法
    @Pointcut("execution(@com.example.mystudy.aop.CheckNet  * *(..))")
    public void executionCheck(){
        Log.e("TAG","执行到executionCheck");
    }

    @Around("executionCheck()")
    public Object check(ProceedingJoinPoint joinPoint) throws Throwable{
        Log.e("TAG","执行到check");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckNet checkNet = signature.getMethod().getAnnotation(CheckNet.class);
        if (checkNet!=null){
            Context pointThis = (Context) joinPoint.getThis();

        }
        return joinPoint.proceed();
    }


    /*********************无参数******************************/
    //这个方法会拦截AopActivity2.noParam()方法的执行
    @Around("execution(* com.example.mystudy.ui.AopActivity2.noParam())")
    public void noParam(ProceedingJoinPoint joinPoint) throws Throwable{
        Log.e("TAG","执行到noParam");
        //方法继续执行
//        joinPoint.proceed();
    }


    //这个方法会拦截CheckNet方法的执行
    @Around("execution(@com.example.mystudy.aop.CheckNet * *(..))")
    public void annotationNoParams(ProceedingJoinPoint joinPoint) throws Throwable{
        Log.e("TAG","执行到CheckNet");
        //方法继续执行
        joinPoint.proceed();
    }

    /***************************有参数****************************************/
    @Around("execution(* com.example.mystudy.ui.AopActivity2.hasParams(..))")
    public void withParams(ProceedingJoinPoint joinPoint)throws Throwable{
        //获取方法上的参数
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            Log.e("TAG","执行到hasParams方法的参数"+arg);
        }
        //1.直接使用原来的参数调用
//        joinPoint.proceed();

        //2.参数经过处理后返回
        joinPoint.proceed(new Object[]{10});
    }

    //这个方法会拦截CheckNet方法的执行
    @Around("execution(@com.example.mystudy.aop.CheckNetWithParams * *(..)) && @annotation(checkNetWithParams)")
    public void annotationWithParams(ProceedingJoinPoint joinPoint,CheckNetWithParams checkNetWithParams) throws Throwable{
        //获取参数
        int value = checkNetWithParams.value();
        Log.e("TAG","执行到CheckNetWithParams的参数->"+value);
        joinPoint.proceed();
    }


}
