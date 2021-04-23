package com.example.mystudy;

public class Test2 {
    boolean a;
    public static void main(String[] args) {
        System.out.print(new Test2().a);
    }

    private static void testList(Integer[] list2) {
        for (int i = 0; i < list2.length; i++) {
            list2[i] = 10 - list2[i];
        }
    }
}
