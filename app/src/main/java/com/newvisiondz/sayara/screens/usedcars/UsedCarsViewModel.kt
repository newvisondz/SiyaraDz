package com.newvisiondz.sayara.screens.usedcars

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.google.gson.JsonArray
import com.google.gson.JsonElement
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
import okhttp3.RequestBody.Companion.asRequestBody
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
    val context: Context = application.applicationContext
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
    var newItemServer = UsedCar()

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
        token.let {
            call.getAllUsedCars(it, 1, 6).enqueue(object : Callback<List<UsedCar>> {
                override fun onFailure(call: Call<List<UsedCar>>, t: Throwable) {}

                override fun onResponse(call: Call<List<UsedCar>>, response: Response<List<UsedCar>>) {
                    tmpDataList.addAll(response.body()!!)
                    _bidsList.value = tmpDataList
                }
            })
        }
    }

    fun performPagination(pageNumber: Int, viewThreshold: Int) {
        token.let {
            call.getAllUsedCars(it, pageNumber, viewThreshold).enqueue(object : Callback<List<UsedCar>> {
                override fun onFailure(call: Call<List<UsedCar>>, t: Throwable) {}

                override fun onResponse(call: Call<List<UsedCar>>, response: Response<List<UsedCar>>) {
                    tmpDataList.addAll(response.body()!!)
                    _bidsList.value = tmpDataList
                }
            })
        }
    }

    fun addItemToList() {

        newItemServer.currentMiles = newCarMiles.value!!
        newItemServer.price = newCarPrice.value!!
        newItemServer.yearOfRegistration = newCarDate.value!!
        tmpDataList.add(newItemServer)
        _bidsList.value = tmpDataList
        createUsedCarInServer(newItemServer, context)

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

    private fun createUsedCarInServer(newItem: UsedCar, context: Context) {
        val partList = mutableListOf<MultipartBody.Part>()
        newItem.uris.forEach {
            val tmpFile = convertBitmapToFile(context, it)
            val requestFile = tmpFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            partList.add(MultipartBody.Part.createFormData("images", tmpFile.name, requestFile))
        }

        call.createUsedCar(
            token,
            partList,
            newItem.manufacturerId,
            newItem.modelId,
            newItem.versionId,
            newItem.yearOfRegistration,
            newItem.currentMiles,
            newItem.price,
            newItem.color,
            newItem.title,
            newItem.manufacturer,
            newItem.model,
            newItem.version
        ).enqueue(object : Callback<UsedCar> {
            override fun onFailure(call: Call<UsedCar>, t: Throwable) {}

            override fun onResponse(call: Call<UsedCar>, response: Response<UsedCar>) {
                if (response.isSuccessful) {
                    val usedRes = response.body()
                    newItem.images.clear()
                    newItem.images = usedRes!!.images
                    newItem.id = usedRes.id
                    //database insertion
                    addToDataBase(newItem)
                    //reset and Ui handling
                    resetLiveDate()
                    newItemServer = UsedCar()
                    insertIsDone.value = true

                }
            }
        })
    }

}