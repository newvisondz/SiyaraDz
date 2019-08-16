package com.newvisiondz.sayara.screens.bids

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.model.Bid

class BidsViewModelFactory(private var app: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BidsViewModel(app) as T
    }
}

class BidsViewModel(application: Application) : AndroidViewModel(application) {
    private val _bidsList = MutableLiveData<MutableList<Bid>>()
    val bidsList: LiveData<MutableList<Bid>>
        get() = _bidsList

    val insertIsDone= MutableLiveData<Boolean>()

    var newItem = Bid()

    val newCarGearBox = MutableLiveData<Int>()
    val newCarMiles = MutableLiveData<Double>()
    val newCarBrand = MutableLiveData<Int>()
    val newCarPrice = MutableLiveData<Double>()
    val newCarModel = MutableLiveData<String>()
    val newCarAdress = MutableLiveData<String>()//todo don't forget to add the adresse attribute
    val newCarDate = MutableLiveData<String>()
    var tmpGearBox = arrayOf<String>()
    var tmpCarBrand = arrayOf<String>()
    var tmpDataList = mutableListOf<Bid>()

    init {
        //todo optimize this code
        insertIsDone.value = null
        getAllBids()
        tmpGearBox = application.resources.getStringArray(R.array.gearboxtypes)
        tmpCarBrand = application.resources.getStringArray(R.array.cars_brands)
    }

    private fun getAllBids() {
        //todo get stuff from server when ready
        tmpDataList.add(Bid(0, "Automatique", 1283.2, "Mercedes", 1220.9, "2018-5-12", "#fff", adresse = "Medea"))
        tmpDataList.add(Bid(0, "Manuelle", 1283.2, "Volvo", 1230.9, "2014-5-12", "#020", adresse = "ALger"))
        tmpDataList.add(Bid(0, "Automatique", 1283.2, "Renault", 1240.9, "2012-5-12", "#fc3", adresse = "Oran"))
        tmpDataList.add(Bid(0, "Manuelle", 1283.2, "Honda", 1250.9, "2016-5-12", "#000", adresse = "Blida"))
        _bidsList.value = tmpDataList
    }

    fun addItemToList() {
        newItem.gearBoxType = tmpGearBox[newCarGearBox.value!!]
        newItem.currrentMiles = newCarMiles.value!!
        newItem.carBrand = tmpCarBrand[newCarBrand.value!!]
        newItem.price = newCarPrice.value!!
        newItem.yearOfRegistration = newCarDate.value!!
        newItem.adresse=newCarAdress.value!!
        newItem.carModel=newCarModel.value!!
        tmpDataList.add(newItem)
        _bidsList.value = tmpDataList
        resetLiveDate()
        newItem=Bid()
        insertIsDone.value = true
    }

    private fun resetLiveDate() {
        newCarGearBox.value = null
        newCarMiles.value = null
        newCarBrand.value = null
        newCarPrice.value = null
        newCarDate.value = null
    }

}