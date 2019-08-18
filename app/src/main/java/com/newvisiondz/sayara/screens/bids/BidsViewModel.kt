package com.newvisiondz.sayara.screens.bids

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.database.getDatabase
import com.newvisiondz.sayara.model.CarInfo
import com.newvisiondz.sayara.model.Model
import com.newvisiondz.sayara.model.UsedCar
import com.newvisiondz.sayara.model.Version
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.getUserToken
import com.newvisiondz.sayara.utils.listFormatter
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BidsViewModelFactory(private var app: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BidsViewModel(app) as T
    }
}

class BidsViewModel(application: Application) : AndroidViewModel(application) {
    private var token: String = ""
    private val userInfo: SharedPreferences =
        application.applicationContext.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    private val _bidsList = MutableLiveData<MutableList<UsedCar>>()
    val bidsList: LiveData<MutableList<UsedCar>>
        get() = _bidsList

    private val _brandList = MutableLiveData<List<CarInfo>>()
    val brandList: LiveData<List<CarInfo>>
        get() = _brandList

    private val _modelList = MutableLiveData<List<CarInfo>>()
    val modelList: LiveData<List<CarInfo>>
        get() = _modelList
    private val _versionList = MutableLiveData<List<CarInfo>>()
    val versionList: LiveData<List<CarInfo>>
        get() = _versionList


    val insertIsDone = MutableLiveData<Boolean>()
    val call = RetrofitClient(application.applicationContext).serverDataApi
    var newItem = UsedCar()

    val newCarMiles = MutableLiveData<Double>()
    val newCarBrand = MutableLiveData<Int>()
    val newCarVersion = MutableLiveData<Int>()
    val newCarPrice = MutableLiveData<Double>()
    val newCarModel = MutableLiveData<Int>()
    val newCarDate = MutableLiveData<String>()
    private var tmpGearBox = arrayOf<String>()
    private var tmpCarBrand = arrayOf<String>()
    private var tmpDataList = mutableListOf<UsedCar>()


    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val dataSource = getDatabase(application.applicationContext).usedCarDao

    init {
        token = getUserToken(userInfo)!!
        //todo optimize this code
        insertIsDone.value = null
        getAllBids()
        tmpGearBox = application.resources.getStringArray(R.array.gearboxtypes)
        tmpCarBrand = application.resources.getStringArray(R.array.cars_brands)
        //---------------------get spinners data :D

        getBrandsList()
    }

    private fun getAllBids() {
        //todo get stuff from server when ready
        tmpDataList.add(UsedCar("", "Automatique", 1283.2, "Mercedes", 1220.9, "2018-5-12", "#fff"))
        tmpDataList.add(UsedCar("", "Manuelle", 1283.2, "Volvo", 1230.9, "2014-5-12", "#020"))
        tmpDataList.add(UsedCar("", "Automatique", 1283.2, "Renault", 1240.9, "2012-5-12", "#fc3"))
        tmpDataList.add(UsedCar("", "Manuelle", 1283.2, "Honda", 1250.9, "2016-5-12", "#000"))
        _bidsList.value = tmpDataList
    }

    fun addItemToList() {
//        newItem.gearBoxType = tmpGearBox[newCarGearBox.value!!]
        newItem.currrentMiles = newCarMiles.value!!
        newItem.carBrand = tmpCarBrand[newCarBrand.value!!]
        newItem.price = newCarPrice.value!!
        newItem.yearOfRegistration = newCarDate.value!!
//        newItem.adresse = newCarAdress.value!!
//        newItem.carModel = newCarModel.value!!    this has to be getting an item from the array
        tmpDataList.add(newItem)
        _bidsList.value = tmpDataList
        addToDataBase(newItem)
        resetLiveDate()
        newItem = UsedCar()
        insertIsDone.value = true
    }

    private fun resetLiveDate() {
        newCarMiles.value = null
        newCarBrand.value = null
        newCarPrice.value = null
        newCarDate.value = null
    }

    private fun addToDataBase(car: UsedCar) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.insertAll(car)
            }
        }
    }


    private fun getBrandsList() {
        token.let {
            call.getAdditionalInfo(it, "brand").enqueue(object : Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful) {
                        val listType = object : TypeToken<MutableList<CarInfo>>() {}.type
                        _brandList.value = listFormatter(response.body()!!, listType, "manufacturers")
                    }
                }
            })
        }
    }

    fun getModelsList(brand: String) {
        token.let {
            call.getAllModels(it, brand, "").enqueue(object : Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {

                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful) {
                        val listType = object : TypeToken<MutableList<CarInfo>>() {}.type
                        _modelList.value= listFormatter(response.body()!!, listType, "models")
                    }
                }

            })
        }
    }

    fun getVersionList(brand: String, model: String) {
        token.let {
            call.getAllVersion(it, brand, model).enqueue(object : Callback<JsonArray> {
                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                }

                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    if (response.isSuccessful) {
                        val listType = object : TypeToken<MutableList<CarInfo>>() {}.type
                        _versionList.value = listFormatter(response.body()!!, listType)
                    }
                }

            })
        }
    }


}