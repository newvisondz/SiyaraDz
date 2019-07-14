package com.newvisiondz.sayara.views.viewModel

import android.app.Application
import android.content.*
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.getUserToken
import com.newvisiondz.sayara.utils.isOnline
import retrofit2.Callback
import retrofit2.Response


class SplashViewModel(application: Application, lifeCycle: Lifecycle) : AndroidViewModel(application),
    LifecycleObserver {
    private val context = application.applicationContext
    private var receiver: NetworkChangeReceiver? = null

    private val _online = MutableLiveData<Boolean>()
    val online: LiveData<Boolean>
        get() = _online

    private val _userConnected = MutableLiveData<Boolean>()
    val userConnected: LiveData<Boolean>
        get() = _userConnected

    private var userInfo: SharedPreferences =
        application.applicationContext.getSharedPreferences("userinfo", Context.MODE_PRIVATE)

    init {
        lifeCycle.addObserver(this)
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        receiver = NetworkChangeReceiver()
        context.registerReceiver(receiver, filter)
    }

    fun navigateToMainActivity() {
        if (_online.value!!) {
            _userConnected.value = !getUserToken(userInfo).equals("Not Found")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unregisterReciever() {
        context.unregisterReceiver(receiver)
    }

    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            _online.value = isOnline(context)
        }
    }

    fun updateNotificationToken(token: String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("token", token)
        val call = RetrofitClient(context).serverDataApi
            .updateUser(getUserToken(userInfo)!!, jsonObject)
        Log.i("FirebaseServiceRes", "sending Token")
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: retrofit2.Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.i("FirebaseServiceRes", response.body().toString())
                }
            }

        })
    }

}

class SplashViewModelFactory(
    private val application: Application, private val lifeCycle: Lifecycle
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(application, lifeCycle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}