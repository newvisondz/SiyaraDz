package com.newvisiondz.sayara.views.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.newvisiondz.sayara.utils.clearUserInfo
import com.newvisiondz.sayara.utils.getUserInfo

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private var userInfo: SharedPreferences? = null
    private var userInfoTmp = arrayOf<String>()
    private val context = application.applicationContext

    private val _logInType = MutableLiveData<String>()
    val logInType: LiveData<String>
        get() = _logInType

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName
    private val _imgUrl = MutableLiveData<String>()
    val imgUrl: LiveData<String>
        get() = _imgUrl
    private val _logOut = MutableLiveData<Boolean>()
    val logOut: LiveData<Boolean>
        get() = _logOut

    init {
        userInfo = context!!.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        userInfoTmp = getUserInfo(userInfo!!)
        _userName.value = userInfoTmp[0]
        if (userInfoTmp[4] == "google") {
            _imgUrl.value = userInfoTmp[1]
        } else {
            _imgUrl.value = "https://graph.facebook.com/${userInfoTmp[1]}/picture?type=large"
        }
    }

    fun getUserInfo() {
        _logInType.value = userInfoTmp[4]
        clearUserInfo(userInfo!!)
    }


}

class ProfileViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}