package com.example.mystudy.widgets

import android.graphics.Point

class ItemPositionModel{

    constructor(){
        center = Point(0, 0)
    }

    constructor(left: Int, top: Int, right: Int, bottom: Int, position: Int):this(){
        this.position = position
        center = Point((left + right) / 2, (top + bottom) / 2)
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
    }

    private var left: Int = 0
    private var top: Int = 0
    private var right: Int = 0
    private var bottom: Int = 0
    private var center: Point? = null
    private var position: Int = 0

    fun getPosition(): Int {
        return position
    }

    fun setPosition(position: Int) {
        this.position = position
    }

    fun getCenter(): Point? {
        return center
    }

    fun setCenter(center: Point) {
        this.center = center
    }

    fun getLeft(): Int {
        return left
    }

    fun setLeft(left: Int) {
        this.left = left
    }

    fun getTop(): Int {
        return top
    }

    fun setTop(top: Int) {
        this.top = top
    }

    fun getRight(): Int {
        return right
    }

    fun setRight(right: Int) {
        this.right = right
    }

    fun getBottom(): Int {
        return bottom
    }

    fun setBottom(bottom: Int) {
        this.bottom = bottom
    }
}