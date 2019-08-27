package com.example.mystudy.data

data class ParentBean(
        val name: String,
        val children: MutableList<ChildBean>
) : IType {
    override fun getType(): Int {
        return 0
    }
}

data class ChildBean(
        val name: String
) : IType {
    override fun getType(): Int {
        return 1
    }
}

interface IType {
    fun getType(): Int
}