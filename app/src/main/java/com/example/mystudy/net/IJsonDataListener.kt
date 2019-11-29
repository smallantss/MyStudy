package com.example.mystudy.net

public interface IJsonDataListener<T> {

    fun onSuccess(t:T)

    fun onFailure()
}