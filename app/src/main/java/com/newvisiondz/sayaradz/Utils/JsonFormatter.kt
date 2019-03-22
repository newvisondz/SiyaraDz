package com.newvisiondz.sayaradz.Utils

import com.google.gson.Gson
import com.google.gson.JsonElement
import java.lang.reflect.Type

class JsonFormatter {
    fun <T> jsonFormatter(jsonString: JsonElement, type: Type, params: String): MutableList<T> {
        var resultList = mutableListOf<T>()
        val json = jsonString.asJsonObject
        val elements = json.getAsJsonArray(params)
        if (elements != null) {
            resultList = Gson().fromJson(elements, type)
        }

        return resultList
    }


}