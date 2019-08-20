package com.newvisiondz.sayara.screens.myusedcars

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.newvisiondz.sayara.database.getDatabase
import com.newvisiondz.sayara.model.UsedCar
import kotlinx.coroutines.*
import okhttp3.internal.userAgent

class UsedCarsViewModelFactory(private var app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return UsedCarsViewModel(app) as T
    }

}

class UsedCarsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataSource = getDatabase(application).usedCarDao

    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val usedCarsList = dataSource.getAds()

    init {
        Log.i("size", "${usedCarsList.value?.size}")
    }

    fun deleteUsedCarAd(carPosition: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                usedCarsList.value?.get(carPosition)?.id?.let {
                    dataSource.deleteOne(it) }
            }
        }
    }
}