package com.newvisiondz.sayara.utils

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import java.lang.reflect.Type


class JsonFormatter {
    fun <T> listFormatter(jsonString: JsonElement, type: Type, params: String): MutableList<T> {
        var resultList = mutableListOf<T>()
        val json = jsonString.asJsonObject
        val elements = json.getAsJsonArray(params)
        if (elements != null) {
            resultList = Gson().fromJson(elements, type)
        }
        return resultList
    }

    companion object {
        @JvmStatic
        fun <T> listFormatter(jsonString: JsonArray, type: Type): MutableList<T> {
            return Gson().fromJson(jsonString, type)
        }
    }
}