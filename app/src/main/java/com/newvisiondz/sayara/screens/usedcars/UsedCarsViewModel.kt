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
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody


class UsedCarsViewModelFactory(private var app: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return UsedCarsViewModel(app) as T
    }
}

class UsedCarsViewModel(application: Application) : AndroidViewModel(application) {
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

    private val _errorObservable = MutableLiveData<Boolean>()
    val errorObservable: LiveData<Boolean>
        get() = _errorObservable

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
        token = getUserToken(userInfo)
        insertIsDone.value = null
        _errorObservable.value = null
        getAllUsedCars()
        getBrandsList()
    }

    fun getAllUsedCars() {
        token.let {
            call.getAllUsedCars(it, 1, 6).enqueue(object : Callback<List<UsedCar>> {
                override fun onFailure(call: Call<List<UsedCar>>, t: Throwable) {}

                override fun onResponse(
                    call: Call<List<UsedCar>>,
                    response: Response<List<UsedCar>>
                ) {
                    tmpDataList.clear()
                    tmpDataList.addAll(response.body()!!)
                    _bidsList.value = tmpDataList
                }
            })
        }
    }

    fun performPagination(pageNumber: Int, viewThreshold: Int) {
        token.let {
            call.getAllUsedCars(it, pageNumber, viewThreshold)
                .enqueue(object : Callback<List<UsedCar>> {
                    override fun onFailure(call: Call<List<UsedCar>>, t: Throwable) {}

                    override fun onResponse(
                        call: Call<List<UsedCar>>,
                        response: Response<List<UsedCar>>
                    ) {
                        tmpDataList.addAll(response.body()!!)
                        _bidsList.value = tmpDataList
                    }
                })
        }
    }

    fun addItemToList() {
        try {
            newItemServer.currentMiles = newCarMiles.value!!
            newItemServer.price = newCarPrice.value!!
            newItemServer.yearOfRegistration = newCarDate.value!!
        } catch (e: Exception) {
            _errorObservable.value = true
        }

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
                        _brandList.value =
                            listFormatter(response.body()!!, listType, "manufacturers")
                    }
                }
            })
        }
    }

    fun getModelsList(brand: String) {
        token.let {
            call.getAllModelsNames(it, brand, "").enqueue(object : Callback<JsonElement> {
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
        var index = 0
        newItem.uris.forEach {
            val tmpFile = convertBitmapToFile(context, it, index)
            val requestFile = tmpFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            partList.add(MultipartBody.Part.createFormData("images", tmpFile.name, requestFile))
            index++
        }
        val manufacturerId = newItem.manufacturerId.toRequestBody("text/plain".toMediaTypeOrNull())
        val modelId = newItem.modelId.toRequestBody("text/plain".toMediaTypeOrNull())
        val versionId = newItem.versionId.toRequestBody("text/plain".toMediaTypeOrNull())
        val manufacturer = newItem.manufacturer.toRequestBody("text/plain".toMediaTypeOrNull())
        val model = newItem.model.toRequestBody("text/plain".toMediaTypeOrNull())
        val version = newItem.version.toRequestBody("text/plain".toMediaTypeOrNull())
        val title = newItem.title.toRequestBody("text/plain".toMediaTypeOrNull())
        val color = newItem.color.toRequestBody("text/plain".toMediaTypeOrNull())
        val registrationDate =
            newItem.yearOfRegistration.toRequestBody("text/plain".toMediaTypeOrNull())

        call.createUsedCar(
            token,
            partList,
            manufacturerId,
            modelId,
            versionId,
            registrationDate,
            newItem.currentMiles,
            newItem.price,
            color,
            title,
            manufacturer,
            model,
            version
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

    fun filterUsedCars(
        q: String,
        maxCurrentMiles: Double = 9999999999.9,
        minCurrentMiles: Double = 0.0,
        maxPrice: Double = 9999999999.9,
        minPrice: Double = 0.0
    ) {
        call.filterUsedCars(token, q, minPrice, maxPrice, minCurrentMiles, maxCurrentMiles)
            .enqueue(object : Callback<List<UsedCar>> {
                override fun onFailure(call: Call<List<UsedCar>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<List<UsedCar>>,
                    response: Response<List<UsedCar>>
                ) {
                    tmpDataList.clear()
                    tmpDataList.addAll(response.body()!!)
                    _bidsList.value = tmpDataList
                }
            })
    }
}