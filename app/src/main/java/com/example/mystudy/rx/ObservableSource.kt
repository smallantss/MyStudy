package com.example.mystudy.rx

 interface ObservableSource<T> {

     //只有一个订阅的方法
    fun subscribe(observer:Observer<T>)
}