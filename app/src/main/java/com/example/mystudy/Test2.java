package com.example.mystudy;

import java.util.ArrayList;
import java.util.List;

public class Test2 {

    public static void main(String[] args){
        String[] list = new String[]{"","",""};
        Integer[] list2 = new Integer[]{0,1,2};

        testList(list2);
        for (Integer integer : list2) {
            System.out.print(integer);
        }
    }

    private static void testList(Integer[] list2) {
        for (int i = 0; i < list2.length; i++) {
            list2[i] = 10-list2[i];
        }
    }
}
