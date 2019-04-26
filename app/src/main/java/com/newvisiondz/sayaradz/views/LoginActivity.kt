package com.newvisiondz.sayaradz.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.services.Auth.FacebookAuthentification
import com.newvisiondz.sayaradz.services.Auth.GoogleAuthentification
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private var callbackManager = CallbackManager.Factory.create()!!
    private var authGoogle: GoogleAuthentification? = null
    private var authFacebook: FacebookAuthentification? = null
    private var userInfo: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressLogin.visibility = View.GONE
        userInfo = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        authFacebook = FacebookAuthentification(this)
        authGoogle = GoogleAuthentification(this)
        authGoogle!!.signInButton.setOnClickListener {
            authGoogle!!.signIn(this)
        }
        loginFb.setReadPermissions("email")
        loginFb.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(this@LoginActivity, "error to Login Facebook", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(loginResult: LoginResult) {
                progressLogin.visibility = View.VISIBLE
                authFacebook!!.signIn(loginResult, coordinator)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == authGoogle!!.REQ_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            this.authGoogle!!.handleResult(result)
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

}
