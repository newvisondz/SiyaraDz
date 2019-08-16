package com.newvisiondz.sayara.screens.brands

import android.app.Application
import androidx.lifecycle.*
import com.newvisiondz.sayara.model.Brand

class BrandsViewModelFactory(private var app: Application, private var lifecycleOwner: LifecycleOwner) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BrandsViewModel(app, lifecycleOwner) as T
    }
}

class BrandsViewModel(application: Application, lifecycleOwner: LifecycleOwner) : AndroidViewModel(application) {
    private val _brandsList = MutableLiveData<MutableList<Brand>>()
    val brandsList: LiveData<MutableList<Brand>>
        get() = _brandsList
    private var appRepository: BrandsRepository? = null

    init {
        appRepository = BrandsRepository.getInstance(getApplication())
        appRepository!!.list.observe(lifecycleOwner, Observer { list ->
            _brandsList.value = list
        })
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