package com.example.mystudy.designpattern

/**
 * 责任链模式
 */
class DutyChainPattern {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
//            handle1()
//            handle2()
//            handle3()

            val wxHandler = WXHandler()
            val qqHandler = QQHandler()
            val otherHandler = OtherHandler()
            wxHandler.nextHandler = qqHandler
            qqHandler.nextHandler = otherHandler
            wxHandler.handle(500)
            wxHandler.handle(1500)
            wxHandler.handle(2500)
            wxHandler.handle(5500)

        }

        private fun handle3() {
            val groupLeader = GroupLeader()
            val departLeader = DepartLeader()
            val bossLeader = BossLeader()
            groupLeader.nextLeader = departLeader
            departLeader.nextLeader = bossLeader
            groupLeader.handleMoney(10000)
        }

        private fun handle2() {
            //3个处理者
            val handler1 = Handler1()
            val handler2 = Handler2()
            val handler3 = Handler3()
            //设置当前节点的下一个处理对象
            handler1.mNextHandler = handler2
            handler2.mNextHandler = handler3
            //3个请求者
            val request1 = Request1()
            val request2 = Request2()
            val request3 = Request3()
            handler1.handleRequest(request2)

        }

        private fun handle1() {
            val handler1 = ConcreteHandler1()
            val handler2 = ConcreteHandler2()
            handler1.nextProcessor = handler2
            handler2.nextProcessor = handler1
            handler1.handleRequest("3")
        }
    }
}

abstract class Handler {
    //下一个节点的处理对象
    lateinit var nextProcessor: Handler

    //处理请求
    abstract fun handleRequest(condition: String)
}

//具体的处理对象1
class ConcreteHandler1 : Handler() {
    //处理请求的条件
    override fun handleRequest(condition: String) {
        if (condition == "1") {
            println("ConcreteHandler1 处理了")
        } else {
            nextProcessor.handleRequest(condition)
        }
    }
}

//具体的处理对象2
class ConcreteHandler2 : Handler() {
    override fun handleRequest(condition: String) {
        if (condition == "2") {
            println("ConcreteHandler2 处理了")
        } else {
            println("没人能处理了")
//            nextProcessor.handleRequest(condition)
        }
    }
}

/*************************************************/

//抽象的处理者
abstract class AbstractHandler {
    //下一个处理者
    var mNextHandler: AbstractHandler? = null

    //获取处理等级
    abstract fun getHandlerLevel(): Int

    //每个处理者具体的处理方式
    abstract fun handle(request: AbstractRequest)

    //处理请求的逻辑
    fun handleRequest(request: AbstractRequest) {
        //判断等级匹配
        if (getHandlerLevel() == request.getRequestLevel()) {
            //具体的处理逻辑
            handle(request)
        } else {
            if (mNextHandler != null) {
                mNextHandler!!.handleRequest(request)
            } else {
                println("找不到处理者")
            }
        }
    }
}

//抽象的请求者
abstract class AbstractRequest(var any: Any) {
    //获取请求级别
    abstract fun getRequestLevel(): Int
}

//具体的请求者
class Request1 : AbstractRequest("Request1") {
    override fun getRequestLevel(): Int {
        return 1
    }
}

class Request2 : AbstractRequest("Request2") {
    override fun getRequestLevel(): Int {
        return 2
    }
}

class Request3 : AbstractRequest("Request3") {
    override fun getRequestLevel(): Int {
        return 3
    }
}

//具体的处理者
class Handler1 : AbstractHandler() {
    override fun getHandlerLevel(): Int {
        return 1
    }

    override fun handle(request: AbstractRequest) {
        println("Handler1处理了请求等级为:${request.getRequestLevel()}的请求")
    }
}

class Handler2 : AbstractHandler() {
    override fun getHandlerLevel(): Int {
        return 2
    }

    override fun handle(request: AbstractRequest) {
        println("Handler2处理了请求等级为:${request.getRequestLevel()}的请求")
    }
}

class Handler3 : AbstractHandler() {
    override fun getHandlerLevel(): Int {
        return 3
    }

    override fun handle(request: AbstractRequest) {
        println("Handler3处理了请求等级为:${request.getRequestLevel()}的请求")
    }
}

/*****************************************************/
//领导处理保报销
abstract class Leader {
    //报销金额上限
    abstract fun getLimit(): Int

    //处理报销
    abstract fun handle(money: Int)

    //上一级领导
    var nextLeader: Leader? = null

    fun handleMoney(money: Int) {
        if (getLimit() < money) {
            if (nextLeader == null) {
                println("处理不了：$money 元")
            } else {
                nextLeader!!.handleMoney(money)
            }
        } else {
            handle(money)
        }
    }
}

class GroupLeader : Leader() {
    override fun getLimit(): Int {
        return 1000
    }

    override fun handle(money: Int) {
        println("小组领导处理：$money 元")
    }
}

class DepartLeader : Leader() {
    override fun getLimit(): Int {
        return 5000
    }

    override fun handle(money: Int) {
        println("部门领导处理：$money 元")
    }
}

class BossLeader : Leader() {
    override fun getLimit(): Int {
        return 8000
    }

    override fun handle(money: Int) {
        println("Boss领导处理：$money 元")
    }
}

/***************************************************************/
interface IHandler{
    fun handle(num: Int)
}
//基类,都可以处理请求
abstract class AbsHandler:IHandler {
    lateinit var nextHandler: AbsHandler
}
//WX处理
class WXHandler : AbsHandler(){
    override fun handle(num: Int) {
        if (num>1000){
            nextHandler.handle(num)
        }else{
            System.out.println("WXHandler:$num")
        }
    }
}

class QQHandler : AbsHandler(){
    override fun handle(num: Int) {
        if (num>2000){
            nextHandler.handle(num)
        }else{
            System.out.println("QQHandler:$num")
        }
    }
}

class OtherHandler : AbsHandler(){
    override fun handle(num:Int) {
        if (num>5000){
            System.out.println("reject:$num")
        }else{
            System.out.println("OtherHandler:$num")
        }
    }
}