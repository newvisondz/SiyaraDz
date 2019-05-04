package com.newvisiondz.sayaradz.repositories


import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayaradz.Utils.JsonFormatter
import com.newvisiondz.sayaradz.Utils.PrefrencesHandler
import com.newvisiondz.sayaradz.model.Brand
import com.newvisiondz.sayaradz.services.RetrofitClient
import retrofit2.Call
import retrofit2.Response

class BrandsRepository private constructor(var context: Context) {
    private val formatter = JsonFormatter()
    private val prefrencesHandler = PrefrencesHandler()
    private val userInfo: SharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    val listType = object : TypeToken<MutableList<Brand>>() {}.type!!
    var list: MutableLiveData<MutableList<Brand>> = MutableLiveData()

    init {
        list = getBrandsData()
    }

    companion object {
        @Volatile
        private var INSTANCE: BrandsRepository? = null

        fun getInstance(context: Context): BrandsRepository {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        BrandsRepository(context)
                }
            }
            return INSTANCE!!
        }
    }


    fun getBrandsData(): MutableLiveData<MutableList<Brand>> {
        val call = RetrofitClient(context)
            .serverDataApi
            .getAllBrands(prefrencesHandler.getUserToken(userInfo)!!, 1, 6, "")

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.i("Nice", t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    try {
                        list.value?.clear()
                        list.value = formatter.listFormatter(response.body()!!, listType, "manufacturers")
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
            .getAllBrands(
                prefrencesHandler.getUserToken(userInfo)!!,
                (pageNumber),
                (viewThreshold),
                ""
            )
        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val tmp: MutableList<Brand> = formatter.listFormatter(response.body()!!, listType, "manufacturers")
                    if (tmp.size != 0) {
                        list.value!!.addAll(tmp)
                    }
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
            .filterBrands(prefrencesHandler.getUserToken(userInfo)!!, q)
        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.i("Exception", "may be server error ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    list.value!!.clear()
//                    val tmp: MutableList<Brand> =
                    list.value=(formatter.listFormatter(response.body()!!, listType, "manufacturers"))
                }
            }
        })
    }
}