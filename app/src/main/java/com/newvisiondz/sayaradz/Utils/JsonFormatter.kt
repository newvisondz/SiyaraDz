package com.newvisiondz.sayaradz.Utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.newvisiondz.sayaradz.model.Model
import java.lang.reflect.Type

class JsonFormatter {
    fun <T> brandsFormatter(jsonString: JsonElement, type: Type, params: String): MutableList<T> {
        var resultList = mutableListOf<T>()
        val json = jsonString.asJsonObject
        val elements = json.getAsJsonArray(params)
        if (elements != null) {
            Log.i("json",elements.toString())
            resultList = Gson().fromJson(elements, type)
        }
        return resultList
    }

    fun  modelsFormatter(jsonString: JsonElement, type: Type, params: String):MutableList<Model> {
        var resultList: MutableList<Model>
        val json = jsonString.asJsonObject
        val elements = json.getAsJsonArray(params)
        resultList =Gson().fromJson(elements,type)
        return resultList
    }


}