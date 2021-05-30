package com.example.okhttp

class MyOkhttpClient{



//    val client = OkHttpClient.Builder().build()
//    val request = Request.Builder().build()
//    val call = client.newCall(request)
//    call.enqueue(object :Callback{
//
//        override fun onFailure(call: Call, e: IOException) {
//
//        }
//
//        override fun onResponse(call: Call, response: Response) {
//
//        }
//    })


    fun newCall(request: MyRequest):MyCall{
        return MyRealCall(this,request)
    }


    private var mUrl: String? = null
    var dispatcher: MyDispatcher? = null

    constructor(){
        Builder()
    }

    private constructor(builder: Builder) : this() {
        mUrl = builder.mUrl
        dispatcher = builder.mDispatcher
    }

    class Builder {

        var mUrl: String? = null
        var mDispatcher: MyDispatcher? = null

        constructor(){
            mDispatcher = MyDispatcher()
        }

        fun dispatcher(dispatcher: MyDispatcher):Builder{
            mDispatcher = dispatcher
            return this
        }

        fun url(url: String): Builder {
            mUrl = url
            return this
        }

        fun build(): MyOkhttpClient {
            return MyOkhttpClient(this)
        }
    }
}