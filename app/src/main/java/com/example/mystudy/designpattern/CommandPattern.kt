package com.example.mystudy.designpattern

class CommandPattern {

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            //命令接收者
            val receiver = Receiver()
            //命令
            val command = ConcreteCommand(receiver)
            //执行者
            val invoker = Invoker(command)
            invoker.action()
        }
    }
}

//接收者类
class Receiver{
    //真正执行具体命令逻辑的方法
    fun action(){
        println("执行具体操作")
    }
}

//抽象命令接口
interface Command{
    //执行具体操作的命令
    fun execute()
}

//具体命令类 持有一个接收者对象引用
class ConcreteCommand(val receiver:Receiver):Command{

    override fun execute() {
        //调用接收者相关方法执行具体逻辑
        receiver.action()
    }
}

//请求者 持有一个命令对象的引用
class Invoker(val command: Command){

    fun action(){
        //调用具体命令对象的相关方法，执行具体命令
        command.execute()
    }
}

