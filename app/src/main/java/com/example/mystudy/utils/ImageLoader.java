package com.example.mystudy.utils;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

public class ImageLoader {

    private static final int MAX_OPACITY = 20;

    private static LinkedHashMap<String, SoftReference<Bitmap>> firstCache =new  LinkedHashMap<String, SoftReference<Bitmap>>(MAX_OPACITY){

        @Override
        protected boolean removeEldestEntry(Entry<String, SoftReference<Bitmap>> eldest) {
            //返回true 表示移除最老的那个对象
            if (this.size()>MAX_OPACITY){
                return true;
            }else {
                //往磁盘添加
                diskCache(eldest.getKey(),eldest.getValue());
                return false;

            }
        }
    };

    //
    private static void diskCache(String key, SoftReference<Bitmap> value) {
        Bitmap bitmap;
//        bitmap.compress()
    }
}
