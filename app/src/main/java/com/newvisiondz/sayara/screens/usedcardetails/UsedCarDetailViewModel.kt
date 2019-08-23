package com.newvisiondz.sayara.screens.usedcardetails

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.newvisiondz.sayara.model.Bid
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.getUserToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalStateException


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
    private val call = RetrofitClient(context).serverDataApi
    private val userInfo: SharedPreferences =
        context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    private var token: String = ""

    private val _createWithSuccess = MutableLiveData<Boolean>()
    val createWithSuccess: LiveData<Boolean>
        get() = _createWithSuccess


    init {
        userInfo.let {
            token = getUserToken(it)
        }
    }

    fun getAllBidsOfCar(carId: String) {
        call.getAllBidsOfUsedCar(token, usedCarId = carId)
            .enqueue(object : Callback<List<Bid>> {
                override fun onFailure(call: Call<List<Bid>>, t: Throwable) {

                }

                override fun onResponse(call: Call<List<Bid>>, response: Response<List<Bid>>) {
                    _bidsList.value = response.body()
                }

            })
    }

    fun createNewBid(carId: String, price: Double) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("car", carId)
        jsonObject.addProperty("price", price)
       try {
           call.createNewBid(token,carId, jsonObject).enqueue(object : Callback<Bid> {
               override fun onFailure(call: Call<Bid>, t: Throwable) {

               }

               override fun onResponse(call: Call<Bid>, response: Response<Bid>) {

               }
           })
       }
       catch (e:IllegalStateException){
           //todo something went wrong
       }
    }

}