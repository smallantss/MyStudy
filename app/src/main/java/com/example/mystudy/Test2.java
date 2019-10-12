package com.example.mystudy;

import com.example.mystudy.aop.CheckNet;
import com.example.mystudy.aop.NetType;

import java.lang.reflect.Method;

public class Test2 {

    public static void main(String[] args){
//        String s = new String("我");
//        testString(s);
//        System.out.println(s);
//        int a = 0;
//        testInt(a);
//        System.out.println(a);
//        Person p = new Person();
//        p.name = "xwy";
//        testObj(p);
//        System.out.println(p.name);


        Method[] methods = Test2.class.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            Class<?>[] parameterTypes = method.getParameterTypes();
            System.out.println("方法名:"+method.getName()+"------------");
            for (Class<?> parameterType : parameterTypes) {
                String paramName = parameterType.getName();
                if (parameterType.equals(boolean.class)){
                    System.out.println(paramName);
                }
            }
        }

    }

    @CheckNet
    public void testAnnotation(int a, String b, NetType netType){

    }

    private void testBool(boolean b){

    }

    private static void testString(String s){
        s="真帅";
    }

    private static void testInt(int s){
        s++;
    }

    private static void testObj(Person p){
        p.name = "aaa";
    }
}

class Person{
    String name;
}
