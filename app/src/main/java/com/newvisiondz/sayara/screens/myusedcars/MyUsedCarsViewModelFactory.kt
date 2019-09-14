package com.newvisiondz.sayara.screens.myusedcars

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.newvisiondz.sayara.database.getDatabase
import com.newvisiondz.sayara.model.UsedCar
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.getUserToken
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsedCarsViewModelFactory(private var app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return UsedCarsViewModel(app) as T
    }

}
class UsedCarsViewModel(application: Application) : AndroidViewModel(application) {
    private val dataSource = getDatabase(application).usedCarDao
    private val context = application.applicationContext
    val token = getUserToken(context.getSharedPreferences("userinfo", Context.MODE_PRIVATE))
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val usedCarsList = dataSource.getAds()
    private val _deletedWithSuccess = MutableLiveData<Boolean>()
    val deletedWithSuccess: LiveData<Boolean>
        get() = _deletedWithSuccess

    init {
        getAllUserUsedCars()
    }

    fun deleteUsedCarAd(carPosition: Int) {
        RetrofitClient(context).serverDataApi.deleteUsedCar(
            token,
            usedCarsList.value?.get(carPosition)!!.id
        )
            .enqueue(
                object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {}

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        if (response.isSuccessful){
                            val res: String = response.body()!!["ok"].asString
                            if (res == "1") {
                                _deletedWithSuccess.value = true
                                uiScope.launch {
                                    withContext(Dispatchers.IO) {
                                        usedCarsList.value?.get(carPosition)?.id?.let {
                                            dataSource.deleteOne(it)
                                        }
                                    }
                                }
                            } else if (res == "false") {
                                _deletedWithSuccess.value = false
                            }
                        }
                    }
                })
    }

    private fun getAllUserUsedCars() {
        RetrofitClient(context).serverDataApi.getUsedCarPerUser(token)
            .enqueue(
                object : Callback<List<UsedCar>> {
                    override fun onFailure(call: Call<List<UsedCar>>, t: Throwable) {}

                    override fun onResponse(
                        call: Call<List<UsedCar>>,
                        response: Response<List<UsedCar>>
                    ) {
                        if (response.isSuccessful) {
                            val list = response.body()!!
                            uiScope.launch {
                                withContext(Dispatchers.IO) {
                                    dataSource.insertAll(* list.toTypedArray())
                                }
                            }
                        }
                    }
                })
    }
}