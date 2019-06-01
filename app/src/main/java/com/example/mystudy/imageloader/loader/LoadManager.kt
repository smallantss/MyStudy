package com.example.mystudy.imageloader.loader

class LoadManager private constructor(){

    //缓存所有支持的loader类型
    var mLoaderMap = HashMap<String,Loader>()

    companion object {
        private var mInstance = LoadManager()

        fun getInstance():LoadManager{
            return mInstance
        }
    }

    init {
        register("http",UrlLoader())
        register("https",UrlLoader())
        register("file",LocalLoader())
    }

    private fun register(s: String, loader: Loader) {
        mLoaderMap[s] = loader
    }

    fun getLoader(schema:String): Loader? {
        return if (mLoaderMap.containsKey(schema)){
            mLoaderMap[schema]
        }else{
            NullLoader()
        }

    }


}