package com.newvisiondz.sayara.screens.usedcardetails

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.newvisiondz.sayara.model.Bid
import com.newvisiondz.sayara.model.User
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

    private val _userInfoServer = MutableLiveData<User>()
    val userInfoServer: LiveData<User>
        get() = _userInfoServer

    private val _bidResponse = MutableLiveData<Boolean>()
    val bidResponse: LiveData<Boolean>
        get() = _bidResponse


    init {
        userInfo.let {
            token = getUserToken(it)
        }
        _createWithSuccess.value = null
        _bidResponse.value = null
    }

    fun getAllBidsOfCar(carId: String) {
        call.getAllBidsOfUsedCar(token, usedCarId = carId)
            .enqueue(object : Callback<List<Bid>> {
                override fun onFailure(call: Call<List<Bid>>, t: Throwable) {

                }

                override fun onResponse(call: Call<List<Bid>>, response: Response<List<Bid>>) {
                    if (response.isSuccessful) {
                        _bidsList.value = response.body()
                    }
                }

            })
    }

    fun createNewBid(carId: String, price: Double) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("car", carId)
        jsonObject.addProperty("price", price)
        try {
//            if (price < )
            call.createNewBid(token, carId, jsonObject).enqueue(object : Callback<Bid> {
                override fun onFailure(call: Call<Bid>, t: Throwable) {

                }

                override fun onResponse(call: Call<Bid>, response: Response<Bid>) {
                    _createWithSuccess.value = true
                }
            })
        } catch (e: IllegalStateException) {
            _createWithSuccess.value = false
            //todo something went wrong
        }
    }

    fun getOwnerInfo(ownerId: String) {
        call.getUserProfileInfo(token, ownerId).enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {

            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    _userInfoServer.value = response.body()
                }
            }
        })
    }

    fun acceptBid(ownerResponse: Boolean, usedCarId: String, bidId: String) {
        val json = JsonObject()
        json.addProperty("accepted", ownerResponse)
        call.acceptBid(token, usedCarId, bidId, json).enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    _bidResponse.value=ownerResponse
                }
            }

        })
    }

}