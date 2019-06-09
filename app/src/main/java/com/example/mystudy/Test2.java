package com.example.mystudy;

import java.util.ArrayList;
import java.util.List;

public class Test2 {

    public static void main(String[] args){
        String s = new String("我");
        testString(s);
        System.out.println(s);
        int a = 0;
        testInt(a);
        System.out.println(a);
        Person p = new Person();
        p.name = "xwy";
        testObj(p);
        System.out.println(p.name);

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
