package com.example.mystudy.rx.darren;

/**
 * Created by hcDarren on 2017/12/3.
 */

public interface Function<T,R> {
    R apply(T t) throws Exception;
}
