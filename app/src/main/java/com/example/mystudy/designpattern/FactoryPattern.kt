package com.example.mystudy.designpattern

import android.util.LruCache

/**
 * 工厂模式
 */
class FactoryPattern {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val product = ConcreteFactory().create()
            product.method()
        }
    }
}

abstract class Product {
    abstract fun method()
}

class ProductA : Product() {
    override fun method() {
        System.out.println("这是ProductA")
    }
}

abstract class Factory {
    abstract fun create(): Product
}

class ConcreteFactory : Factory() {
    override fun create(): Product {
        return ProductA()
    }
}

class ReflectFactory {

    fun <T : Product> createProduct(clz: Class<T>): T {
        val instance = Class.forName(clz.name).newInstance() as T
        return instance
    }
}

interface IOHandler {
    fun save(name: String, data: String)
    fun get(name: String): String
}

class MemoryIOHandler : IOHandler {
    private val mCache = LruCache<String, String>(10 * 1024 * 1024)
    override fun get(name: String): String {
        return mCache.get(name)
    }

    override fun save(name: String, data: String) {
        mCache.put(name, data)
    }
}

class PreferenceIOHandler : IOHandler {

    override fun save(name: String, data: String) {

    }

    override fun get(name: String): String {
        return ""
    }

}
//
//enum class IOType {
//    MEMORY,
//    PREFERENCE
//}
//
//class IOHandlerFactory {
//    companion object {
//        fun create(type: IOType): IOHandler {
//            return when (type) {
//                IOType.MEMORY -> MemoryIOHandler()
//                IOType.PREFERENCE -> PreferenceIOHandler()
//            }
//        }
//    }
//}

//interface IOFactory{
//    fun createIOHandler():IOHandler
//}
//class MemoryFactory:IOFactory{
//    override fun createIOHandler(): IOHandler {
//        return MemoryIOHandler()
//    }
//}
//class PreferenceFactory:IOFactory{
//    override fun createIOHandler(): IOHandler {
//        return PreferenceIOHandler()
//    }
//}
//
class Use{

    fun use(){
        val ioHandler = IOHandlerFactory.getMemoryIOHandler()
        ioHandler.save("name","XWY")
        ioHandler.get("name")
    }
}

class IOHandlerFactory {
    companion object {
        fun <T:IOHandler> createIOHandler(clazz: Class<T>) :IOHandler{
            return clazz.newInstance()
        }

        fun getMemoryIOHandler():IOHandler{
            return createIOHandler(MemoryIOHandler::class.java)
        }

        fun getPreferenceIOHandler():IOHandler{
            return createIOHandler(PreferenceIOHandler::class.java)
        }
    }
}