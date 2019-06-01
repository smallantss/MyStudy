package com.example.mystudy.imageloader.config

import android.media.Image
import com.example.mystudy.imageloader.cache.BitmapCache
import com.example.mystudy.imageloader.policy.ILoadPolicy

/**
 * 配置信息
 */
class ImageLoaderConfig private constructor(){

    //缓存策略
    var bitmapCache:BitmapCache?=null
    //加载粗略
    var loadPolicy:ILoadPolicy?=null
    //线程数
    var threadCount = Runtime.getRuntime().availableProcessors()
    //显示配置
    var displayConfig:DisplayConfig?=null

    companion object {

        class Builder{

            var config:ImageLoaderConfig = ImageLoaderConfig()

            //缓存策略
            fun setCachePolicy(cache:BitmapCache):Builder{
                config.bitmapCache = cache
                return this
            }

            //加载策略
            fun setLoadPolicy(loadPolicy: ILoadPolicy):Builder{
                config.loadPolicy = loadPolicy
                return this
            }

            //线程数
            fun setThreadCount(count:Int):Builder{
                config.threadCount = count
                return this
            }

            fun setLoadingImg(resId:Int):Builder{
                config.displayConfig?.loadingImg = resId
                return this
            }

            fun setFailImg(resId:Int):Builder{
                config.displayConfig?.failImg = resId
                return this
            }

            fun build():ImageLoaderConfig{

                return config
            }

        }
    }


}