package com.newvisiondz.sayaradz.views.viewModel

import android.app.Application
import android.content.*
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.*
import com.newvisiondz.sayaradz.utils.getUserToken
import com.newvisiondz.sayaradz.utils.isOnline


class SplashViewModel(application: Application, lifeCycle: Lifecycle) : AndroidViewModel(application),
    LifecycleObserver {
    private val context = application.applicationContext
    private var isConnected = false
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
        _userConnected.value = !getUserToken(userInfo).equals("Not Found")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unregisterReciever() {
        context.unregisterReceiver(receiver)
        Log.i("Splash","On destroy")
    }

    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            _online.value=isOnline(context)
            Log.i("Splash",_online.value.toString())
        }
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