package com.newvisiondz.sayara.screens.bids

import android.app.Application
import androidx.lifecycle.*
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

    init {
        getAllBids()
    }

    private fun getAllBids() {
        _bidsList.value = mutableListOf(
            Bid(0, "Automatique", 1283.2, "Mercedes", 1220.9, "2018-5-12", "#fff", adresse = "Medea"),
            Bid(0, "Manuelle", 1283.2, "Volvo", 1230.9, "2014-5-12", "#020", adresse = "ALger"),
            Bid(0, "Automatique", 1283.2, "Renault", 1240.9, "2012-5-12", "#fc3", adresse = "Oran"),
            Bid(0, "Manuelle", 1283.2, "Honda", 1250.9, "2016-5-12", "#000", adresse = "Blida")
        )
    }

}