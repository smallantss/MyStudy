package com.example.okhttp

interface MyCallBack {

    fun onFailure(call: MyCall)

    fun onResponse(call: MyCall,response:MyResponse)
}