package com.newvisiondz.sayara.views.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.getUserInfo
import com.newvisiondz.sayara.utils.getUserToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFormViewModelFactory(private var application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ProfileFormViewModel(application) as T
    }

}

class ProfileFormViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private var userInfo: SharedPreferences? = null

    private val _updateWithSuccess = MutableLiveData<Boolean>()
    val updateWithSuccess: LiveData<Boolean>
        get() = _updateWithSuccess

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val birthDate = MutableLiveData<String>()
    val email = MutableLiveData<String>()

    init {
        userInfo = context!!.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        val tmp = getUserInfo(userInfo!!)
        firstName.value = tmp[3]
        lastName.value = tmp[0]
        email.value = tmp[2]

        _updateWithSuccess.value = null
    }

    fun updateUserInfo() {
        val jsonObject= JsonObject()
        jsonObject.addProperty("firstName", firstName.value)
        jsonObject.addProperty("lastName", lastName.value)
        jsonObject.addProperty("address", address.value)
        jsonObject.addProperty("phone", phone.value)
        jsonObject.addProperty("birthDate", birthDate.value)

        val call = RetrofitClient(context!!)
            .serverDataApi
            .updateUser(getUserToken(userInfo!!)!!,jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res: String = response.body()!!.get("ok").asString
                    if (res == "1") {
                        _updateWithSuccess.value = true
                    } else if (res == "0") {
                        _updateWithSuccess.value = false
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}