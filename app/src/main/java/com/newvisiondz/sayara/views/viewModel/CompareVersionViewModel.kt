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

class CompareVersionViewModel(application: Application, manufacturer: String, modelId: String) :
    AndroidViewModel(application) {
    private var userInfo: SharedPreferences? = null
    private val context: Context = application.applicationContext

    private val _versionsList = MutableLiveData<MutableList<Version>>()
    val versionList: LiveData<MutableList<Version>>
        get() = _versionsList

    private val _versionDetails1 = MutableLiveData<Version>()
    val versionDetails1: LiveData<Version>
        get() = _versionDetails1

    private val _versionDetails2 = MutableLiveData<Version>()
    val versionDetails2: LiveData<Version>
        get() = _versionDetails2

    private val _doneGettingResult = MutableLiveData<Boolean>()
    val doneGettingResult: LiveData<Boolean>
        get() = _doneGettingResult

    init {
        userInfo = application.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        getAllVersions(manufacturer, modelId)
        _doneGettingResult.value = null
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
                    val list: MutableList<Version> = JsonFormatter.listFormatter(response.body()!!, listType)
                    _versionsList.value = list
                }
            }

        })
    }

    fun getVersionDetails(manufacturer: String, modelId: String, versionId: String, spinnerId: Int) {
        _doneGettingResult.value = false
        val call = RetrofitClient(context).serverDataApi.getVersionDetails(
            getUserToken(userInfo!!)!!, manufacturer, modelId, versionId
        )
        call.enqueue(object : Callback<Version> {
            override fun onFailure(call: Call<Version>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Version>, response: Response<Version>) {
                if (response.isSuccessful) {
                    if (spinnerId == 0) _versionDetails1.value = response.body()!!
                    else _versionDetails2.value = response.body()!!
                    _doneGettingResult.value = true
                }
            }

        })
    }

}

class CompareVersionViewModelFactory(
    private val application: Application, private val manufacturer: String, private val modelId: String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompareVersionViewModel::class.java)) {
            return CompareVersionViewModel(application, manufacturer, modelId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}