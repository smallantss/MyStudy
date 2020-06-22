package com.example.mystudy.rx.darren;

/**
 * Created by hcDarren on 2017/12/3.
 */

public interface Consumer<T> {
    void onNext(T item) throws Exception;
}
