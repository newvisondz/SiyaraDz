package com.newvisiondz.sayaradz.views.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.newvisiondz.sayaradz.model.Brand
import com.newvisiondz.sayaradz.repositories.BrandsRepository

class BrandsViewModelFactory(private var app: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BrandsViewModel(app) as T
    }
}

class BrandsViewModel(application: Application) :
    AndroidViewModel(application) {
    var brandsList: LiveData<MutableList<Brand>>
    private var appRepository: BrandsRepository? = null

    init {
        appRepository = BrandsRepository.getInstance(getApplication())
        brandsList = appRepository!!.list
    }

    fun performPagination(pageNumber: Int, viewThreshold: Int) {
        appRepository!!.performPagination(pageNumber, viewThreshold)
    }

    fun getBrandsData() {
        appRepository!!.getBrandsData()
    }

    fun filterBrands(filter: String) {
        appRepository!!.filterBrands(filter)
    }
}