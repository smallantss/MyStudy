package com.example.mystudy.rx.map

interface Function<T, R> {
    fun apply(t: T): R
}