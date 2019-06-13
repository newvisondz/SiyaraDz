package com.newvisiondz.sayaradz.views.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.newvisiondz.sayaradz.services.RetrofitClient
import com.newvisiondz.sayaradz.utils.getUserInfo
import com.newvisiondz.sayaradz.utils.getUserToken
import org.json.JSONObject
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
        firstName.value=tmp[3]
        lastName.value=tmp[0]
        email.value=tmp[2]

        _updateWithSuccess.value = false
    }

    fun updateUserInfo() {
        val paramObject = JSONObject()
        paramObject.put("firstName", firstName.value)
        paramObject.put("lastName", lastName.value)
        paramObject.put("address", address.value)
        paramObject.put("phone", phone.value)
        paramObject.put("birthDate", birthDate.value)
        val call = RetrofitClient(context!!)
            .serverDataApi
            .updateUser(getUserToken(userInfo!!)!!, paramObject.toString())
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res: String = response.body()!!.get("ok").asString
                    if (res == "1") {
                        _updateWithSuccess.value = true
                    }
                }
            }
//            .apply(
//            RequestOptions()
//            .placeholder(R.drawable.loading_animation)
//            .error(R.drawable.ic_broken_image)
//            )


            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}