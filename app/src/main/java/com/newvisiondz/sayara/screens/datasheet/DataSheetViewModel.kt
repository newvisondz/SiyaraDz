package com.newvisiondz.sayara.screens.datasheet

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayara.model.CarInfo
import com.newvisiondz.sayara.model.Version
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.getUserToken
import com.newvisiondz.sayara.utils.listFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataSheetViewModelFactory(private var app: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DataSheetViewModel(app) as T
    }

}

class DataSheetViewModel(application: Application) : AndroidViewModel(application) {
    private val _versionList = MutableLiveData<MutableList<Version>>()
    val versionsList: LiveData<MutableList<Version>>
        get() = _versionList

    private val context = application.applicationContext
    var token = ""
    val call = RetrofitClient(application.applicationContext).serverDataApi

    val color = MutableLiveData<String>()
    val engineType = MutableLiveData<String>()
    val enginePower = MutableLiveData<String>()
    val fuel = MutableLiveData<String>()
    val places = MutableLiveData<String>()


    init {
        token = getUserToken(context.getSharedPreferences("userinfo", Context.MODE_PRIVATE))
    }

    fun getVersionDetail(
        manufacturerId: String,
        modelId: String,
        versionId: String
    ) {
        token.let {
            call.getVersionDetails(
                token,
                manufacturerId,
                modelId,
                versionId
            ).enqueue(
                object : Callback<Version> {
                    override fun onFailure(call: Call<Version>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<Version>, response: Response<Version>) {
                        if (response.isSuccessful) {
                            response.body()?.let { it1 -> handleData(it1) }

                        }
                    }
                }
            )
        }
    }

    fun getVersionList(brand: String, model: String) {
        token.let {
            call.getAllVersion(it, brand, model).enqueue(object : Callback<JsonArray> {
                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                }

                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    if (response.isSuccessful) {
                        val listType = object : TypeToken<MutableList<Version>>() {}.type
                        _versionList.value = listFormatter(response.body()!!, listType)
                    }
                }

            })
        }
    }

    fun handleData(newVersion: Version) {
        for (item in newVersion.options) {
            when (item.name) {
                "places" -> {
                    places.value = item.values[0].value
                }
                "Type du carburant" -> {
                    fuel.value = item.values[0].value
                }
                "moteur" -> {
                    enginePower.value = item.values[0].value
                }
                "Boite" -> {
                    engineType.value = item.values[0].value
                }
            }
        }
        color.value = newVersion.colors[0].value
    }
}