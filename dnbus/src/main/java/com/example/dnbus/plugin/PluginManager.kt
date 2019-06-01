package com.example.dnbus.plugin

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import dalvik.system.DexClassLoader

/**
 * 插件化
 * 1.加载资源Resources
 * 2.加载类 ClassLoader
 * 3.插件Activity的生命周期
 */
class PluginManager private constructor(){

    companion object {

        private var mInstance = PluginManager()

        fun getInstance() = mInstance
    }

    lateinit var mContext:Context
    var mPluginApk:PluginApk?=null

    fun initContext(context: Context){
        mContext = context.applicationContext
    }

    fun loadApk(path:String){
        val packageInfo = mContext.packageManager.getPackageArchiveInfo(path,
                PackageManager.GET_ACTIVITIES or PackageManager.GET_SERVICES)
        packageInfo?:return

        //加载ClassLoader
        val dexClassLoader = createDexClassLoader(path)
        //加载Resource之前，先创建AssetManager
        val assetManager = createManager(path)
        val resource = createResource(assetManager)
        mPluginApk = PluginApk(packageInfo,dexClassLoader,resource)
    }

    private fun createManager(path: String): AssetManager {
        //hide 反射
//        val manager = AssetManager()
        val assetManager = AssetManager::class.java.newInstance()
        val addAssetPath = assetManager.javaClass.getDeclaredMethod("addAssetPath", String::class.java)
        addAssetPath.invoke(assetManager,path)
        return assetManager
    }

    private fun createResource(assetManager:AssetManager): Resources {
        val curResource = mContext.resources
        return Resources(assetManager,curResource.displayMetrics,curResource.configuration)
    }

    /**
     * 创建访问APK的ClassLoader
     */
    private fun createDexClassLoader(path:String):DexClassLoader{
        // 路径  优化后输出目录  动态so库 classLoader
        val file = mContext.getDir("odex",Context.MODE_PRIVATE)
        return DexClassLoader(path,file.absolutePath,null,mContext.classLoader)
    }


}