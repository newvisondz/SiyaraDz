package com.newvisiondz.sayara.screens.myoffers

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.newvisiondz.sayara.model.UserBid
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.getUserToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UsedCarsViewModelFactory(private var app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MyOffersViewModel(app) as T
    }

}

class MyOffersViewModel(application: Application) : AndroidViewModel(application) {
    private val _userBidsList = MutableLiveData<MutableList<UserBid>>()
    private val userBidsListTmp = mutableListOf<UserBid>()
    val userBidsList: LiveData<MutableList<UserBid>>
        get() = _userBidsList

    private val context = application.applicationContext
    val call = RetrofitClient(context).serverDataApi
    val userInfo: SharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    private val token = getUserToken(userInfo)

    init {
        getUserBids()
    }

    private fun getUserBids() {
        call.getUserBid(token).enqueue(object :
            Callback<MutableList<UserBid>> {
            override fun onFailure(call: Call<MutableList<UserBid>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MutableList<UserBid>>,
                response: Response<MutableList<UserBid>>
            ) {
                userBidsListTmp.addAll(response.body()!!)
                _userBidsList.value = userBidsListTmp
            }
        })
    }

    fun removeBid(viewHolderPosition: Int) {
        val carId = userBidsList.value?.get(viewHolderPosition)?.usedCar?.id
        val bidId = userBidsList.value?.get(viewHolderPosition)?.id
        call.removeBid(token, carId!!, bidId!!).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful){
                    userBidsListTmp.removeAt(viewHolderPosition)
                    _userBidsList.value=userBidsListTmp
                    val res: String = response.body()!!["ok"].asString
                }
            }

        })
    }
}