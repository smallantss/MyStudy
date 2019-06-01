package com.example.mystudy.imageloader.request

import android.widget.ImageView
import com.example.mystudy.imageloader.config.DisplayConfig
import com.example.mystudy.imageloader.loader.SimpleImageLoader
import com.example.mystudy.imageloader.policy.ILoadPolicy
import java.lang.ref.SoftReference

/**
 * 网络请求类
 */
class BitmapRequest(imageView: ImageView, val imageUrl: String,
                    val displayConfig: DisplayConfig?,
                    var mListener: SimpleImageLoader.Companion.ImageListener?) : Comparable<BitmapRequest> {
    override fun compareTo(other: BitmapRequest): Int {
        return loadPolicy!!.compartorto(this,other)
    }

    var imageViewSoft: SoftReference<ImageView>? = null
    //MD5的图片路径
    var imageUriMd5: String? = null

    init {
        imageViewSoft = SoftReference(imageView)
        //设置可见的ImageView的tag为要下载的图片路径
        imageView.tag = imageUrl

    }

    //加载策略
    var loadPolicy: ILoadPolicy? = SimpleImageLoader.getInstance().config?.loadPolicy

    var serialNo: Int = 0

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as BitmapRequest
        if (serialNo != other.serialNo) return false
        return if (loadPolicy != null) {
            loadPolicy!! == other.loadPolicy
        } else {
            other.loadPolicy == null
        }
    }

    override fun hashCode(): Int {
        val result = if (loadPolicy != null) {
            loadPolicy.hashCode()
        } else {
            0
        }
        return result + serialNo
    }


}