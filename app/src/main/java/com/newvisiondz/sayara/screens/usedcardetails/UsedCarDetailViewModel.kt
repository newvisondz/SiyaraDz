package com.newvisiondz.sayara.screens.usedcardetails

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.google.gson.JsonElement
import com.newvisiondz.sayara.model.Bid
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.getUserToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UsedCarDetailViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsedCarDetailViewModel::class.java)) {
            return UsedCarDetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class UsedCarDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _bidsList = MutableLiveData<List<Bid>>()
    val bidsList: LiveData<List<Bid>>
        get() = _bidsList
    private val context = application.applicationContext
    private val userInfo: SharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    private var token: String = ""

    init {
        userInfo.let {
            token = getUserToken(it)
        }
    }

    fun getAllBidsOfCar(carId: String) {
        RetrofitClient(context).serverDataApi.getAllBidsOfUsedCar(token, usedCarId = carId)
            .enqueue(object : Callback<List<Bid>> {
                override fun onFailure(call: Call<List<Bid>>, t: Throwable) {

                }

                override fun onResponse(call: Call<List<Bid>>, response: Response<List<Bid>>) {
                    _bidsList.value=response.body()
                }

            })
    }
}