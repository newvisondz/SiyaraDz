package com.newvisiondz.sayara.views.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayara.model.Version
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.JsonFormatter
import com.newvisiondz.sayara.utils.getUserToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompareVersionViewModel(application: Application) : AndroidViewModel(application) {
    private var userInfo: SharedPreferences? = null
    private val context: Context = application.applicationContext

    private val _versionsList1 = MutableLiveData<MutableList<Version>>()
    val versionList1: LiveData<MutableList<Version>>
        get() = _versionsList1
    private val _versionsList2 = MutableLiveData<MutableList<Version>>()
    val versionList2: LiveData<MutableList<Version>>
        get() = _versionsList2


    init {
        userInfo = application.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    }

    fun getAllVersions(manufacturer: String, modelId: String) {
        val call = RetrofitClient(context).serverDataApi.getAllVersion(
            getUserToken(userInfo!!)!!, manufacturer, modelId
        )
        call.enqueue(object : Callback<JsonArray> {
            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.isSuccessful) {
                    val listType = object : TypeToken<MutableList<Version>>() {}.type
                    _versionsList1.value = JsonFormatter.listFormatter(response.body()!!, listType)
                }
            }

        })
    }

}

class CompareVersionViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return CompareVersionViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}