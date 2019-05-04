package com.newvisiondz.sayaradz.views.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.newvisiondz.sayaradz.model.Model
import com.newvisiondz.sayaradz.repositories.ModelsRepository

class ModelsViewModelsFactory(private var app: Application,var brandName :String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ModelsViewModel(app,brandName) as T
    }

}

class ModelsViewModel(app: Application,var brandName:String) : AndroidViewModel(app) {
    private var modelsRepository: ModelsRepository? = null
    var modelsList: MutableLiveData<MutableList<Model>>

    init {
        modelsRepository = ModelsRepository.getInstance(getApplication(),brandName)
        modelsList = modelsRepository!!.list
    }
}
