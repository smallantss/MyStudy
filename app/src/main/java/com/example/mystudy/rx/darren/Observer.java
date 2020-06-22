package com.example.mystudy.rx.darren;

import io.reactivex.annotations.NonNull;

/**
 * Created by hcDarren on 2017/12/2.
 * 观察者
 */
public interface Observer<T> {
    void onSubscribe();
    void onNext(@NonNull T item);
    void onError(@NonNull Throwable e);
    void onComplete();
}
