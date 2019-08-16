package com.newvisiondz.sayara.screens.models

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayara.model.Model
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.getUserToken
import com.newvisiondz.sayara.utils.listFormatter
import retrofit2.Call
import retrofit2.Response

class ModelsRepository(private var context: Context) {

    private var _list: MutableLiveData<MutableList<Model>> = MutableLiveData()
    val list: LiveData<MutableList<Model>>
        get() = _list
    private val userInfo: SharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    val listType = object : TypeToken<MutableList<Model>>() {}.type!!

    companion object {
        @Volatile
        private var INSTANCE: ModelsRepository? = null

        fun getInstance(context: Context): ModelsRepository {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        ModelsRepository(context)
                }
            }
            return INSTANCE!!
        }
    }

    // : MutableLiveData<MutableList<Model>>  a return ?
    fun getModelData(brandName: String) {
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
                        _list.value = listFormatter(response.body()!!, listType, "models")
                    } catch (e: Exception) {
                        Log.i("Nice", e.message)
                    }
                }
            }
        })
    }

    fun performPagination(pageNumber: Int, viewThreshold: Int, brandName: String) {
        val call = RetrofitClient(context)
            .serverDataApi
            .getAllModels(
                getUserToken(userInfo)!!,
                brandName,
                pageNumber,
                viewThreshold
            )
        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val tmp: MutableList<Model> = listFormatter(response.body()!!, listType, "models")
                    if (tmp.size != 0) {
                        _list.value!!.addAll(tmp)
                    }
                    _list.value = list.value
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

    fun filterBrands(q: String, brandName: String) {
        val call = RetrofitClient(context)
            .serverDataApi
            .getAllModels(getUserToken(userInfo)!!, brandName, q)
        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.i("Exception", "may be server error ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    _list.value!!.clear()
                    _list.value = (listFormatter(response.body()!!, listType, "models"))
                }
            }
        })
    }
}