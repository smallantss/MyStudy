package com.example.okhttp

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MyDispatcher {

    private var executors:ExecutorService?=null

    constructor(){
        if (executors==null){
            executors = Executors.newCachedThreadPool()
        }
    }


    fun enqueue(call: MyAsyncCall){
        executors?.execute(call)
    }

}
