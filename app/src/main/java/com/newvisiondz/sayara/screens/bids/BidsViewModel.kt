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

    val newCarGearBox = MutableLiveData<Int>()
    val newCarMiles = MutableLiveData<Double>()
    val newCarBrand = MutableLiveData<Int>()
    val newCarPrice = MutableLiveData<Double>()
    val newCarColor = MutableLiveData<String>()
    val newCarImage = MutableLiveData<String>()
    val newCarDate = MutableLiveData<String>()
    var tmp = arrayOf<String>()

    init {
        getAllBids()
        tmp = application.resources.getStringArray(R.array.gearboxtypes)
    }

    private fun getAllBids() {
        _bidsList.value = mutableListOf(
            Bid(0, "Automatique", 1283.2, "Mercedes", 1220.9, "2018-5-12", "#fff", adresse = "Medea"),
            Bid(0, "Manuelle", 1283.2, "Volvo", 1230.9, "2014-5-12", "#020", adresse = "ALger"),
            Bid(0, "Automatique", 1283.2, "Renault", 1240.9, "2012-5-12", "#fc3", adresse = "Oran"),
            Bid(0, "Manuelle", 1283.2, "Honda", 1250.9, "2016-5-12", "#000", adresse = "Blida")
        )
    }

    fun addItemToList() {
        Log.i(
            "databindingtest",
            " miles ==${newCarMiles.value.toString()} gearBox== ${tmp[newCarGearBox.value?.toInt()!!]}"
        )
    }

}