package com.newvisiondz.sayaradz.Utils

import com.google.gson.Gson
import com.google.gson.JsonElement
import java.lang.reflect.Type

class JSONFormatter {
    fun <T> jsonFormatter(jsonString: JsonElement, type: Type, params:String): MutableList<T> {
        val resultList:MutableList<T>
        val json =jsonString.asJsonObject
        val elements =json.getAsJsonArray(params)
        resultList =Gson().fromJson(elements,type)
        return resultList
    }
}