package com.newvisiondz.sayaradz.views.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.newvisiondz.sayaradz.model.Model
import com.newvisiondz.sayaradz.services.RetrofitClient
import com.newvisiondz.sayaradz.utils.getUserToken
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
    val context: Context = application.applicationContext

    private val _model = MutableLiveData<Model>()
    val model: LiveData<Model>
        get() = _model

    init {
        userInfo = application.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    }

    fun getAllVersions(manufacturer: String, modelId: String) {
        val call = RetrofitClient(context).serverDataApi.getModelDetails(
            getUserToken(userInfo!!)!!, manufacturer, modelId
        )
        call.enqueue(object : Callback<Model> {
            override fun onFailure(call: Call<Model>, t: Throwable) {
                t.printStackTrace()
            }
            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                if (response.isSuccessful) {
                    _model.value=response.body()!!
                }
            }

        })
    }
}