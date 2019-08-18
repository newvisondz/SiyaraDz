package com.newvisiondz.sayara.screens.bids

import android.app.Application
import androidx.lifecycle.*
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.database.getDatabase
import com.newvisiondz.sayara.model.UsedCar
import kotlinx.coroutines.*

class BidsViewModelFactory(private var app: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BidsViewModel(app) as T
    }
}

class BidsViewModel(application: Application) : AndroidViewModel(application) {
    private val _bidsList = MutableLiveData<MutableList<UsedCar>>()
    val bidsList: LiveData<MutableList<UsedCar>>
        get() = _bidsList

    val insertIsDone = MutableLiveData<Boolean>()

    var newItem = UsedCar()

    val newCarGearBox = MutableLiveData<Int>()
    val newCarMiles = MutableLiveData<Double>()
    val newCarBrand = MutableLiveData<Int>()
    val newCarPrice = MutableLiveData<Double>()
    val newCarModel = MutableLiveData<String>()
    val newCarAdress = MutableLiveData<String>()//todo don't forget to add the adresse attribute
    val newCarDate = MutableLiveData<String>()
    private var tmpGearBox = arrayOf<String>()
    private var tmpCarBrand = arrayOf<String>()
    private var tmpDataList = mutableListOf<UsedCar>()


    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val dataSource= getDatabase(application.applicationContext).usedCarDao

    init {
        //todo optimize this code
        insertIsDone.value = null
        getAllBids()
        tmpGearBox = application.resources.getStringArray(R.array.gearboxtypes)
        tmpCarBrand = application.resources.getStringArray(R.array.cars_brands)
    }

    private fun getAllBids() {
        //todo get stuff from server when ready
        tmpDataList.add(UsedCar("", "Automatique", 1283.2, "Mercedes", 1220.9, "2018-5-12", "#fff", adresse = "Medea"))
        tmpDataList.add(UsedCar("", "Manuelle", 1283.2, "Volvo", 1230.9, "2014-5-12", "#020", adresse = "ALger"))
        tmpDataList.add(UsedCar("", "Automatique", 1283.2, "Renault", 1240.9, "2012-5-12", "#fc3", adresse = "Oran"))
        tmpDataList.add(UsedCar("", "Manuelle", 1283.2, "Honda", 1250.9, "2016-5-12", "#000", adresse = "Blida"))
        _bidsList.value = tmpDataList
    }

    fun addItemToList() {
        newItem.gearBoxType = tmpGearBox[newCarGearBox.value!!]
        newItem.currrentMiles = newCarMiles.value!!
        newItem.carBrand = tmpCarBrand[newCarBrand.value!!]
        newItem.price = newCarPrice.value!!
        newItem.yearOfRegistration = newCarDate.value!!
        newItem.adresse = newCarAdress.value!!
        newItem.carModel = newCarModel.value!!
        tmpDataList.add(newItem)
        _bidsList.value = tmpDataList
        addToDataBase(newItem)
        resetLiveDate()
        newItem = UsedCar()
        insertIsDone.value = true
    }

    private fun resetLiveDate() {
        newCarGearBox.value = null
        newCarMiles.value = null
        newCarBrand.value = null
        newCarPrice.value = null
        newCarDate.value = null
    }

    private fun addToDataBase(car:UsedCar) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dataSource.insertAll(car)
            }
        }
    }
}