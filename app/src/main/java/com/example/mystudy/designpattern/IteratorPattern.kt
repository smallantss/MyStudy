package com.example.mystudy.designpattern

class IteratorPattern {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val userInfo = UserInfo("XWY","25")
            var result = isUserExists(userInfo,QQSystem())
            if (result==null){
                result = isUserExists(userInfo,WXSystem())
            }
            System.out.println(result?.name)
        }

        fun isUserExists(userInfo: UserInfo, iterator: Iterator<UserInfo>): UserInfo? {
            while (iterator.hasNext()) {
                val info = iterator.next()
                if (info == userInfo) {
                    return userInfo
                }
            }
            return null
        }
    }
}
//使用迭代器作为中间组件
interface Iterator<T> {
    //是否有下一个及下一个
    fun hasNext(): Boolean
    fun next(): T
}
//存在多个用户，判断哪个用户
class QQSystem : Iterator<UserInfo> {
    private val users = ArrayList<UserInfo>()
    private var index = 0
    override fun hasNext(): Boolean {
        return index < users.size
    }
    override fun next(): UserInfo {
        return users[index++]
    }
}
//存在多个用户，判断哪个用户
class WXSystem : Iterator<UserInfo> {
    private val users = emptyArray<UserInfo>()
    private var index = 0
    override fun hasNext(): Boolean {
        return index < users.size
    }
    override fun next(): UserInfo {
        return users[index++]
    }
}
data class UserInfo(
        val name: String, val id: String
)