package com.example.mystudy.utils;

import android.text.TextUtils;

public class Calculate {

    private UserManager userManager = new UserManager();

    public static void main(String[] args) {

    }

    public int add(int a, int b) {
        return a + b;
    }

    public int multiply(int a,int b){
        return a*b;
    }

    public void login(String name,String password){
        if (TextUtils.isEmpty(name)) return;
        if (TextUtils.isEmpty(password)) return;
        userManager.performLogin(name, password);
    }

    static  class UserManager{

        public void performLogin(String name,String password){

        }
    }
}
