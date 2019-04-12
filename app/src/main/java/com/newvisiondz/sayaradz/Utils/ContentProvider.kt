package com.newvisiondz.sayaradz.Utils

import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayaradz.model.Model
import com.newvisiondz.sayaradz.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContentProvider {


     fun <T> getModelsContent(brandName:String):MutableList<T> {
        lateinit var models:MutableList<T>
        var jsonFormatter=JsonFormatter()
        val call = RetrofitClient()
            .serverDataApi
            .getAllModels(
                "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjYWU2ZjEzZmQ1ZTZlMDAxODVkODY2YiIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTU0OTM1NTczLCJleHAiOjE1NTU1NDAzNzN9.p0WrgJDm4TafU5qZ6ddow9zwcDUQSSxodM-iUiUc4zA"
                , brandName
            )

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val listType = object : TypeToken<MutableList<Model>>() {}.type
                    try {
                        models = jsonFormatter.listFormatter(response.body()!!, listType, "models")
                    } catch (e: Exception) {

                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                t.printStackTrace()
            }
        })
        return models
    }
}