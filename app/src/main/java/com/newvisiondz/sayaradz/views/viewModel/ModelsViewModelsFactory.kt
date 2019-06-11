package com.newvisiondz.sayaradz.views.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.newvisiondz.sayaradz.model.Model
import com.newvisiondz.sayaradz.repositories.ModelsRepository

class ModelsViewModelsFactory(private var app: Application, private var lifeCycleOwner: LifecycleOwner) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ModelsViewModel(app, lifeCycleOwner) as T
    }

}

class ModelsViewModel(app: Application, lifeCycleOwner: LifecycleOwner) : AndroidViewModel(app) {
    private var modelsRepository: ModelsRepository? = null
    private val _modelsList = MutableLiveData<MutableList<Model>>()
    val modelsList: LiveData<MutableList<Model>>
        get() = _modelsList

    init {
        modelsRepository = ModelsRepository.getInstance(getApplication())
        modelsRepository!!.list.observe(lifeCycleOwner, Observer { newList ->
            _modelsList.value = newList
        })
//        _modelsList =
    }

    fun performPagination(pageNumber: Int, viewThreshold: Int, brandName: String) {
        modelsRepository!!.performPagination(pageNumber, viewThreshold, brandName)
    }

    fun getModelData(brandName: String) {
        modelsRepository!!.getModelData(brandName)
    }

    fun filterModelsData(filterString: String, brandName: String) {
        modelsRepository!!.filterBrands(filterString, brandName)
    }
}
