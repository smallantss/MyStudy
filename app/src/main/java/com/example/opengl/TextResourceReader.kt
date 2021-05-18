package com.example.opengl

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

fun readTextFileFromResource(context: Context, resId: Int): String {
    val body = StringBuilder()
    context.resources.openRawResource(resId).use {
        InputStreamReader(it).use {
            BufferedReader(it).use {
                var nextLine: String? = it.readLine()
                while (nextLine != null) {
                    body.append(nextLine)
                    body.append('\n')
                    nextLine = it.readLine()
                }
            }
        }
    }
    return body.toString()
}