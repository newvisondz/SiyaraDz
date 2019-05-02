package com.newvisiondz.sayaradz.views.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import com.newvisiondz.sayaradz.model.Brand
import com.newvisiondz.sayaradz.services.AppRepository

class BrandsViewModelFactory(private var app: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BrandsViewModel(app) as T
    }
}

class BrandsViewModel(application: Application) : AndroidViewModel(application ) {
    var brandsList: LiveData<MutableList<Brand>>
    private var appRepository: AppRepository? = null

    init {
        Log.i("Nice","ViewModelCalled")
        appRepository = AppRepository.getInstance(getApplication())
        Log.i("Nice","done getting data")
        brandsList = appRepository!!.list
    }
}