package com.newvisiondz.sayara.views.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayara.model.Command
import com.newvisiondz.sayara.model.Version
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.JsonFormatter
import com.newvisiondz.sayara.utils.getUserToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VersionsViewModelFactory(var application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VersionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VersionsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class VersionsViewModel(application: Application) : AndroidViewModel(application) {
    private var userInfo: SharedPreferences? = null
    private val context: Context = application.applicationContext

    private val _version = MutableLiveData<Version>()
    val version: LiveData<Version>
        get() = _version

    private val _commandDetails = MutableLiveData<Command>()
    val commandDetails: LiveData<Command>
        get() = _commandDetails

    private val _versionList = MutableLiveData<MutableList<Version>>()
    val versionList: LiveData<MutableList<Version>>
        get() = _versionList

    private val _commandConfirmed = MutableLiveData<Boolean>()
    val commandConfirmed: LiveData<Boolean>
        get() = _commandConfirmed
    val price:LiveData<Double> = Transformations.map(commandDetails) {
        it.price
    }

    init {
        userInfo = application.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        _commandConfirmed.value = null
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
                    _versionList.value = JsonFormatter.listFormatter(response.body()!!, listType)
                }
            }

        })
    }


    fun getVersionDetails(manufacturer: String, modelId: String, versionId: String) {
        val call = RetrofitClient(context).serverDataApi.getVersionDetails(
            getUserToken(userInfo!!)!!, manufacturer, modelId, versionId
        )
        call.enqueue(object : Callback<Version> {
            override fun onFailure(call: Call<Version>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Version>, response: Response<Version>) {
                if (response.isSuccessful) {
                    _version.value = response.body()!!
                }
            }

        })
    }

    fun sendCommand(manufacturer: String, modelId: String, versionId: String, queries: MutableList<String>) {
        val call = RetrofitClient(context).serverDataApi.sendUserCommand(
            getUserToken(userInfo!!)!!, manufacturer, modelId, versionId, queries
        )
        call.enqueue(object : Callback<Command> {
            override fun onFailure(call: Call<Command>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Command>, response: Response<Command>) {
                _commandDetails.value = response.body()
//                _price.value = response.body()?.price
            }
        })
    }

    fun confirmCommande(carId: String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("vehicle", carId)
        val call = RetrofitClient(context).serverDataApi.confirmUserCommande(
            getUserToken(userInfo!!)!!, jsonObject
        )
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
//                    _commandConfirmed.value = (response.body()?.get("ok")?.asString == "1")
                    _commandConfirmed.value = true
                    //TODO navigate to Tabs
                }
            }

        })
    }
}