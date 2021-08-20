package com.example.mystudy.java.loader;

public class Test {

    public static void main(String[] args) {
        byte a =1;
        byte bit1 = (byte)((a>>7)&1);
        System.out.println(bit1);
    }
}
