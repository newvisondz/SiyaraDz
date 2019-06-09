package com.newvisiondz.sayaradz.views.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.newvisiondz.sayaradz.services.auth.FacebookAuthentification
import com.newvisiondz.sayaradz.services.auth.GoogleAuthentification

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private var context = application.applicationContext


    private var authFacebook: FacebookAuthentification? = null
    private var callbackManager = CallbackManager.Factory.create()!!
    private var userInfo: SharedPreferences? = null
    var authGoogle: GoogleAuthentification? = null

    init {
        userInfo = application.applicationContext.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        authFacebook = FacebookAuthentification(application)
//        authGoogle = GoogleAuthentification(context)
    }

    fun googleSignIn(context: Context) {
        authGoogle!!.signIn(context)
    }

    fun facebookSignIn() {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
//                Toast.makeText(this@LoginActivity, "error to Login Facebook", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(loginResult: LoginResult) {
//                binding.progressLogin.visibility = View.VISIBLE
                authFacebook!!.signIn(loginResult)
            }
        })
    }
}

class LoginViewModelFactory(private var app: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LoginViewModel(app) as T
    }

}
