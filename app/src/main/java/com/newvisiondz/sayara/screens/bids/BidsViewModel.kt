package com.newvisiondz.sayara.screens.bids

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayara.database.getDatabase
import com.newvisiondz.sayara.model.CarInfo
import com.newvisiondz.sayara.model.UsedCar
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.convertBitmapToFile
import com.newvisiondz.sayara.utils.getUserToken
import com.newvisiondz.sayara.utils.listFormatter
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    val context = application.applicationContext
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
    val newCarPrice = MutableLiveData<Double>()
    val newCarDate = MutableLiveData<String>()
    private var tmpDataList = mutableListOf<UsedCar>()


    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val dataSource = getDatabase(application.applicationContext).usedCarDao

    init {
        token = getUserToken(userInfo)!!
        insertIsDone.value = null
        getAllBids()
        getBrandsList()
    }

    private fun getAllBids() {
        //todo get stuff from server when ready
        tmpDataList.add(
            UsedCar(
                "",
                123.4,
                "asne",
                2731.3,
                "2017-12-12",
                "#fc3",
                carModel = "haosid",
                version = "asdhadas"
            )
        )
        tmpDataList.add(
            UsedCar(
                "",
                123423.4,
                "asne",
                2731.3,
                "2017-12-12",
                "#fc3",
                carModel = "haosid",
                version = "asdhadas"
            )
        )
        tmpDataList.add(
            UsedCar(
                "",
                7342.4,
                "asne",
                2731.3,
                "2017-12-12",
                "#fc3",
                carModel = "haosid",
                version = "asdhadas"
            )
        )
        _bidsList.value = tmpDataList
    }

    fun addItemToList() {

        newItem.currrentMiles = newCarMiles.value!!
        newItem.price = newCarPrice.value!!
        newItem.yearOfRegistration = newCarDate.value!!
        tmpDataList.add(newItem)
        _bidsList.value = tmpDataList
        createUsedCarinServer(newItem, context)
//        addToDataBase(newItem)
//        resetLiveDate()
        newItem = UsedCar()
        insertIsDone.value = true
    }

    private fun resetLiveDate() {
        newCarMiles.value = null
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
                        _modelList.value = listFormatter(response.body()!!, listType, "models")
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

    fun createUsedCarinServer(newItem: UsedCar, context: Context) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("manufacturer", newItem.carBrandId)
        jsonObject.addProperty("model", newItem.carModel)
        jsonObject.addProperty("version", newItem.version)
        jsonObject.addProperty("registrationDate", newItem.yearOfRegistration)
        jsonObject.addProperty("currrentMiles", newItem.currrentMiles)
        jsonObject.addProperty("minPrice", newItem.price)
        jsonObject.addProperty("color", newItem.color)

        val file = convertBitmapToFile(context, newItem.bitmap!!)

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val part = MultipartBody.Part.createFormData("images", file.name, requestFile)

        call.createUsedCar(
            token,
            part,
            newItem.carBrandId,
            newItem.carModel,
            newItem.version,
            newItem.yearOfRegistration,
            newItem.currrentMiles,
            newItem.price,
            newItem.color
        ).enqueue(object : Callback<UsedCar> {
            override fun onFailure(call: Call<UsedCar>, t: Throwable) {}

            override fun onResponse(call: Call<UsedCar>, response: Response<UsedCar>) {
                if (response.isSuccessful) {
                    Log.i("response", response.body()?.color)
                }
            }
        })
    }

}