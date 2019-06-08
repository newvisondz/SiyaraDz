package com.newvisiondz.sayaradz.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayaradz.model.Model
import com.newvisiondz.sayaradz.services.RetrofitClient
import com.newvisiondz.sayaradz.utils.JsonFormatter
import com.newvisiondz.sayaradz.utils.getUserToken
import retrofit2.Call
import retrofit2.Response

class ModelsRepository(private var context: Context, var brandName: String) {

    var list: MutableLiveData<MutableList<Model>> = MutableLiveData()
    private val formatter = JsonFormatter()
    private val userInfo: SharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    val listType = object : TypeToken<MutableList<Model>>() {}.type!!

    init {
        list = getModelData()
    }

    companion object {

        @Volatile
        private var INSTANCE: ModelsRepository? = null

        fun getInstance(context: Context, brandName: String): ModelsRepository {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = ModelsRepository(context, brandName)
                }
            }
            return INSTANCE!!
        }
    }

    fun getModelData(): MutableLiveData<MutableList<Model>> {
        val call = RetrofitClient(context)
            .serverDataApi
            .getAllModels(getUserToken(userInfo)!!, brandName, 1, 2)

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.i("Nice", t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    try {
                        list.value?.clear()
                        list.value = formatter.listFormatter(response.body()!!, listType, "models")
                    } catch (e: Exception) {
                        Log.i("Nice", e.message)
                    }
                }
            }
        })
        return list
    }

    fun performPagination(pageNumber: Int, viewThreshold: Int) {
        val call = RetrofitClient(context)
            .serverDataApi
            .getAllModels(getUserToken(userInfo)!!,
                brandName,
                pageNumber,
                viewThreshold
            )
        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val tmp: MutableList<Model> = formatter.listFormatter(response.body()!!, listType, "models")
                    if (tmp.size != 0) {
                        list.value!!.addAll(tmp)
                    }
                    //TODO if that  doesn't work use second list
                    list.value = list.value
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

    fun filterBrands(q: String) {
        val call = RetrofitClient(context)
            .serverDataApi
            .getAllModels(getUserToken(userInfo)!!,brandName, q)
        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.i("Exception", "may be server error ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    list.value!!.clear()
                    list.value = (formatter.listFormatter(response.body()!!, listType, "models"))
                }
            }
        })
    }
}