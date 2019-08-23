package com.newvisiondz.sayara.screens.myoffers

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.newvisiondz.sayara.model.Bid
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
    private val _userBidsList = MutableLiveData<List<UserBid>>()
    val userBidsList:LiveData<List<UserBid>>
        get() = _userBidsList

    private val context=application.applicationContext
    val userInfo: SharedPreferences =context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    private val token = getUserToken(userInfo)

    init {
        getUserBids()
    }

    private fun getUserBids(){
        RetrofitClient(context).serverDataApi.getUserBid(token).enqueue(object :
            Callback<List<UserBid>> {
            override fun onFailure(call: Call<List<UserBid>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<UserBid>>, response: Response<List<UserBid>>) {
                _userBidsList.value=response.body()
            }
        })
    }
}