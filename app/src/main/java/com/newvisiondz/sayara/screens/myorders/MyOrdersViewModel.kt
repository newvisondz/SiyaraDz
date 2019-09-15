package com.newvisiondz.sayara.screens.myorders

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayara.model.CarInfo
import com.newvisiondz.sayara.model.CommandConfirmed
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.getUserToken
import com.newvisiondz.sayara.utils.listFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOrdersViewModelFactory(private var app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MyOrdersViewModel(app) as T
    }

}

class MyOrdersViewModel(application: Application) : AndroidViewModel(application) {
    private val _myOrders = MutableLiveData<MutableList<CommandConfirmed>>()
    val myOrders: LiveData<MutableList<CommandConfirmed>>
        get() = _myOrders

    private val context = application.applicationContext
    val call = RetrofitClient(context).serverDataApi
    val userInfo: SharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    private val token = getUserToken(userInfo)

    init {
        getMyPreviousOrders()
    }
    private fun getMyPreviousOrders() {
        call.getUserCommands(token).enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val listType = object : TypeToken<MutableList<CommandConfirmed>>() {}.type
                    _myOrders.value = response.body()?.let { listFormatter(it, listType, "commandes") }
                }
            }

        })
    }

     fun sendPayementTokentoBackend(commandId: String, creditCardToken: String) {
        val json = JsonObject()
        json.addProperty("token", creditCardToken)
        call.sendCreditCardToken(token, commandId, body = json)
            .enqueue(object : Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {

                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    Log.i("payment", response.body().toString())
                }

            })
    }
}