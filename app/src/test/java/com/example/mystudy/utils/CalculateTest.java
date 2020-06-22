package com.example.mystudy.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalculateTest {

    private Calculate calculate;

    @Before
    public void init(){
        calculate = new Calculate();
    }

    @Test
    public void add() {
        int res = calculate.add(1, 2);
        Assert.assertEquals(res,3);
    }




    @Test
    public void multiply() {
        int res = calculate.multiply(2, 4);
        Assert.assertEquals(res,6);
    }

    @Test
    public void login() {
        
    }
}