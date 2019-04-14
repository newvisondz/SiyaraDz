package com.newvisiondz.sayaradz.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.newvisiondz.sayaradz.services.Auth.FacebookAuthentification
import com.newvisiondz.sayaradz.services.Auth.GoogleAuthentification
import kotlinx.android.synthetic.main.content_login.*
import com.facebook.GraphRequest
import android.util.Log
import com.newvisiondz.sayaradz.R


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private var callbackManager = CallbackManager.Factory.create()!!
    private var authGoogle: GoogleAuthentification? = null
    private var authFacebook: FacebookAuthentification? = null
    private var userInfo: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        userInfo = getSharedPreferences("userinfo", Context.MODE_PRIVATE)

        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.APP_STATE))
            .requestServerAuthCode(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        authFacebook = FacebookAuthentification(this)
        authGoogle = GoogleAuthentification(this, signInOptions)
        authGoogle!!.signInButton.setOnClickListener {
            authGoogle!!.signIn(this)
        }
        loginFb.setReadPermissions("email")
        loginFb.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
            }

            override fun onSuccess(loginResult: LoginResult) {
//                authFacebook!!.signIn(loginResult, coordinator)
                Log.i("Main","doing")
                val request = GraphRequest.newMeRequest(
                    loginResult.accessToken
                ) { `object`, response ->
                    Log.v("Main", response.toString())
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,email,gender, birthday")
                request.parameters = parameters
                request.executeAsync()
            }
        })
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, "Une faible connection internet ", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == authGoogle!!.REQ_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            this.authGoogle!!.handleResult(result)
        }
    }
}
