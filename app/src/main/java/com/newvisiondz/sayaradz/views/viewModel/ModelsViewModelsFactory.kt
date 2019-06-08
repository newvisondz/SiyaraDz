package com.newvisiondz.sayaradz.views.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.newvisiondz.sayaradz.model.Model
import com.newvisiondz.sayaradz.repositories.ModelsRepository

class ModelsViewModelsFactory(private var app: Application, var brandName: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ModelsViewModel(app, brandName) as T
    }

}

class ModelsViewModel(app: Application, brandName: String) : AndroidViewModel(app) {
    private var modelsRepository: ModelsRepository? = null
    var modelsList: MutableLiveData<MutableList<Model>>

    init {
        modelsRepository = ModelsRepository.getInstance(getApplication(), brandName)
        modelsList = modelsRepository!!.list
    }

    fun performPagination(pageNumber: Int, viewThreshold: Int) {
        modelsRepository!!.performPagination(pageNumber, viewThreshold)
    }

    fun getModelData() {
        modelsRepository!!.getModelData()
    }

    fun filterModelsData(filterString: String) {
        modelsRepository!!.filterBrands(filterString)
    }
}
