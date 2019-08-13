package com.newvisiondz.sayara.repositories


import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayara.model.Brand
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.getUserToken
import com.newvisiondz.sayara.utils.listFormatter
import retrofit2.Call
import retrofit2.Response

class BrandsRepository private constructor(var context: Context) {
    private val userInfo: SharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    val listType = object : TypeToken<MutableList<Brand>>() {}.type!!
    private val _list: MutableLiveData<MutableList<Brand>> = MutableLiveData()
    val list: LiveData<MutableList<Brand>>
        get() = _list

    init {
        getBrandsData()
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


    fun getBrandsData() {
        val call = RetrofitClient(context)
            .serverDataApi
            .getAllBrands(getUserToken(userInfo)!!, 1, 6, "")

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    _list.value = listFormatter(response.body()!!, listType, "manufacturers")
                }
            }
        })
    }

    fun performPagination(pageNumber: Int, viewThreshold: Int) {
        val call = RetrofitClient(context)
            .serverDataApi
            .getAllBrands(
                getUserToken(userInfo)!!,
                (pageNumber),
                (viewThreshold),
                ""
            )
        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val tmp: MutableList<Brand> = listFormatter(response.body()!!, listType, "manufacturers")
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
            .filterBrands(getUserToken(userInfo)!!, q)
        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    _list.value!!.clear()
                    _list.value = (listFormatter(response.body()!!, listType, "manufacturers"))
                }
            }
        })
    }
}