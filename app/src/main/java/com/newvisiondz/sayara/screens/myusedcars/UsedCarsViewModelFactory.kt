package com.newvisiondz.sayara.screens.myusedcars

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.JsonElement
import com.newvisiondz.sayara.database.getDatabase
import com.newvisiondz.sayara.model.UsedCar
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.getUserToken
import kotlinx.coroutines.*
import okhttp3.internal.userAgent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class UsedCarsViewModelFactory(private var app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return UsedCarsViewModel(app) as T
    }

}

class UsedCarsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataSource = getDatabase(application).usedCarDao
    private val context = application.applicationContext
    val token = getUserToken(context.getSharedPreferences("userinfo", Context.MODE_PRIVATE))
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val usedCarsList = dataSource.getAds()


    fun deleteUsedCarAd(carPosition: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                usedCarsList.value?.get(carPosition)?.id?.let {
                    dataSource.deleteOne(it)
                }
            }
        }
        RetrofitClient(context).serverDataApi.deleteUsedCar(token!!, usedCarsList.value?.get(carPosition)!!.id)
            .enqueue(
                object : Callback<JsonElement> {
                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {}

                    override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                        Log.i("responseDel", response.body().toString())
                    }
                })
    }
}