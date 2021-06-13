package com.example.campaigns.util

import com.google.gson.Gson
import okio.buffer
import okio.source
import java.lang.reflect.Type

object Util {

    fun <T> parseJsonFileToObject(fileName: String, type: Type): T? {
        val json = getJsonStringFromFile(fileName)
        return Gson().fromJson(json, type)
    }

    fun getJsonStringFromFile(fileName: String): String {
        val inputStream = this::class.java.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream!!.source().buffer()
        return source.readString(Charsets.UTF_8)
    }
} 