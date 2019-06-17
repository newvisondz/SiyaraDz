package com.newvisiondz.sayaradz.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.databinding.ActivityLoginBinding
import com.newvisiondz.sayaradz.services.auth.FacebookAuthentification
import com.newvisiondz.sayaradz.services.auth.GoogleAuthentification


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private var callbackManager = CallbackManager.Factory.create()!!
    private var authGoogle: GoogleAuthentification? = null
    private var authFacebook: FacebookAuthentification? = null
    private var userInfo: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        userInfo = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        authFacebook = FacebookAuthentification(this)
        authGoogle = GoogleAuthentification(this)
        binding.loging.setOnClickListener {
            authGoogle!!.signIn(this)
        }

        binding.loginFb.setOnClickListener {
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    authFacebook!!.signIn(result!!)
                }
                override fun onCancel() {
                }
                override fun onError(error: FacebookException?) {
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GoogleAuthentification.REQ_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            this.authGoogle!!.handleResult(result)
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

}
