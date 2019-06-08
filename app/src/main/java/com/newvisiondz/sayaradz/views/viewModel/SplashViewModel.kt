package com.newvisiondz.sayaradz.views.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.newvisiondz.sayaradz.utils.getUserToken
import com.newvisiondz.sayaradz.utils.isOnline

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val _online = MutableLiveData<Boolean>()
    val online: LiveData<Boolean>
        get() = _online

    private val _userConnected = MutableLiveData<Boolean>()
    val userConnected: LiveData<Boolean>
        get() = _userConnected

    private var userInfo: SharedPreferences =
        application.applicationContext.getSharedPreferences("userinfo", Context.MODE_PRIVATE)

    fun checkInternet() {
        _online.value = isOnline(context)
    }

    fun navigateToMainActivity() {
        Log.i("Splash", getUserToken(userInfo))
        _userConnected.value = !getUserToken(userInfo).equals("Not Found")
    }

}

class SplashViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}