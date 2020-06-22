package com.example.mystudy.designpattern.singleton;

public class Singleton1 {

    private static volatile Singleton1 mInstance ;

    private Singleton1(){

    }

    public static Singleton1 getInstance(){
        if (mInstance==null){
            synchronized (Singleton1.class){
                if (mInstance==null){
                    mInstance = new Singleton1();
                }
            }
        }
        return mInstance;
    }
}
