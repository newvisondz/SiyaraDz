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
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = AppRepository(context)
                }
            }
            return INSTANCE!!
        }
    }


    private fun getBrandsData(): MutableLiveData<MutableList<Brand>> {
        val call = RetrofitClient(context)
            .serverDataApi
            .getAllBrands(prefrencesHandler.getUserToken(userInfo)!!, 1, 6, "")

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.i("Nice", t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    Log.i("Nice", "Response with success")
                    val listType = object : TypeToken<MutableList<Brand>>() {}.type
                    try {

//                        (context.applicationContext as Activity).progressBar.visibility = View.GONE
                        list.value = formatter.listFormatter(response.body()!!, listType, "manufacturers")
                    } catch (e: Exception) {
                        Log.i("Nice", e.localizedMessage)
                    }
                }
            }
        })
        return list
    }

    fun performPagination(pageNumber: Int, viewThreshold: Int) {
//        (context as Activity).progressBar.visibility = View.VISIBLE
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
                    val listType = object : TypeToken<List<Brand>>() {}.type
                    lateinit var tmp: MutableList<Brand>
                    tmp = formatter.listFormatter(response.body()!!, listType, "fabricants")
                    if (tmp.size != 0) {
                        list.value!!.addAll(tmp)
                    }
//                    (context as Activity).progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}