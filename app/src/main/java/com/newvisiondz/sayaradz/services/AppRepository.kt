package com.newvisiondz.sayaradz.services


import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayaradz.Utils.JsonFormatter
import com.newvisiondz.sayaradz.Utils.PrefrencesHandler
import com.newvisiondz.sayaradz.model.Brand
import retrofit2.Call
import retrofit2.Response

class AppRepository private constructor(var context: Context) {
    private val formatter = JsonFormatter()
    private val prefrencesHandler = PrefrencesHandler()
    private val userInfo: SharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)

    var list: MutableLiveData<MutableList<Brand>> = MutableLiveData()

    init {
        list = getBrandsData()
        Log.i("Nice","done getting data")
    }
//
//    private object Holder {
//        val INSTANCE = AppRepository()
//    }
//
//    companion object {
//        val instance: AppRepository by lazy { Holder.INSTANCE }
//    }

    companion object {
        @Volatile
        private var INSTANCE: AppRepository? = null
        fun getInstance(context: Context): AppRepository {
            Log.i("Nice","get instance got called")
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = AppRepository(context)
                }
            }
            Log.i("Nice","instance got called")
            return INSTANCE!!
        }
    }


    private fun getBrandsData(): MutableLiveData<MutableList<Brand>> {
        Log.i("Nice","app repo Got called")
        val call = RetrofitClient(context)
            .serverDataApi
            .getAllBrands(prefrencesHandler.getUserToken(userInfo)!!)

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.i("Nice", t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    Log.i("Nice","Response with success")
                    val listType = object : TypeToken<MutableList<Brand>>() {}.type
                    try {
                        list.value =formatter.listFormatter(response.body()!!, listType, "manufacturers")
                    }
                    catch (e:Exception){
                        Log.i("Nice",e.localizedMessage)
                    }
                }
            }
        })
        return list
    }
}