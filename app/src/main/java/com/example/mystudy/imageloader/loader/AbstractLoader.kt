package com.example.mystudy.imageloader.loader

import android.graphics.Bitmap
import com.example.mystudy.imageloader.cache.BitmapCache
import com.example.mystudy.imageloader.request.BitmapRequest

/**
 * 抽象的加载器
 * 子类实现具体的加载方法
 */
abstract class AbstractLoader  :Loader{

    //拿到用户自定义的缓存策略
    var bitmapCache:BitmapCache = SimpleImageLoader.getInstance().config!!.bitmapCache!!

    //拿到显示策略
    var displayConfig =  SimpleImageLoader.getInstance().config!!.displayConfig!!

    //抽象加载图片的方法，为了区分本地和网络加载
    abstract fun onLoad(request: BitmapRequest): Bitmap?

    override fun loadImage(request: BitmapRequest) {
        //从缓存中取bitmap
        var bitmap = bitmapCache.get(request)
        if (bitmap==null){
            //显示默认加载图片
            showLoadingImage(request)
            //开始真正加载图片
            bitmap = onLoad(request)
            //缓存图片
            cacheBitmap(request,bitmap)

            deliverToUiThread(request,bitmap)
        }
    }

    private fun showLoadingImage(request: BitmapRequest) {
        //是否配置了加载图
        if (!hadLoadingPlaceHolder()){
            val imageView = request.imageViewSoft?.get()
            imageView?.let {
                it.post {
                    it.setImageResource(displayConfig.loadingImg)
                }
            }
        }else{

        }
    }

    private fun hadLoadingPlaceHolder(): Boolean {
        return displayConfig!=null && displayConfig.loadingImg>0
    }

    private fun cacheBitmap(request: BitmapRequest, bitmap: Bitmap?) {
        if (request!=null && bitmap!=null){
            synchronized(AbstractLoader::class.java){
                bitmapCache.put(request,bitmap)
            }
        }
    }

    private fun deliverToUiThread(request: BitmapRequest, bitmap: Bitmap?) {
        val imageView = request.imageViewSoft?.get()
        imageView?.run {
            post {
                updateImageView(request,bitmap)
            }
        }

    }

    private fun updateImageView(request: BitmapRequest, bitmap: Bitmap?) {
        val imageView = request.imageViewSoft?.get()
        imageView?.run {
            if (bitmap!=null && imageView.tag == request.imageUrl){
                //防止加载图片错位
                imageView.setImageBitmap(bitmap)
            }
            if (bitmap==null && displayConfig!=null && displayConfig.failImg!=-1){
                imageView.setImageResource(displayConfig.failImg)
            }
            //监听
            if (request.mListener!=null){
                request.mListener?.onComplete(imageView,bitmap!!,request.imageUrl)
            }
        }
    }
}