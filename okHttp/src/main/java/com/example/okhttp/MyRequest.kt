package com.example.okhttp

import java.util.*
import kotlin.collections.HashMap

class MyRequest {


    var headers: HashMap<String, Any>? = null

    constructor() {
        Builder()
    }

    constructor(builder: MyRequest.Builder) {
        headers = builder.headers
    }


    class Builder {

        constructor() {

        }


        val headers = HashMap<String, Any>()

        fun addHeader(key: String, value: Any): Builder {
            headers[key] = value
            return this
        }

        fun build(): MyRequest {
            return MyRequest(this)
        }
    }


}