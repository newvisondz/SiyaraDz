package com.newvisiondz.sayaradz.views.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.newvisiondz.sayaradz.model.Brand
import com.newvisiondz.sayaradz.services.AppRepository

class BrandsViewModelFactory(private var app: Application, var pageNumber: Int, var viewThreshold: Int) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BrandsViewModel(app, pageNumber, viewThreshold) as T
    }
}

class BrandsViewModel(application: Application, var pageNumber: Int, var viewThreshold: Int) :
    AndroidViewModel(application) {
    var brandsList: LiveData<MutableList<Brand>>
    private var appRepository: AppRepository? = null

    init {
        appRepository = AppRepository.getInstance(getApplication())
        brandsList = appRepository!!.list
    }

    fun performPagination(pageNumber: Int, viewThreshold: Int) {
        appRepository!!.performPagination(pageNumber, viewThreshold)
    }

}