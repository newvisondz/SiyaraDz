package com.newvisiondz.sayaradz.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.services.Auth.GoogleAuthentification
import com.newvisiondz.sayaradz.services.RetrofitClient
import kotlinx.android.synthetic.main.content_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private var callbackManager = CallbackManager.Factory.create()!!
    private var authentification: GoogleAuthentification? = null
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

        authentification = GoogleAuthentification(this, signInOptions)
        authentification!!.signInButton.setOnClickListener {
            Log.i("sendreq", "buttonreq")
            authentification!!.signIn(this)
        }
//        authentification!!.signOutButton.setOnClickListener {
//            authentification!!.signOut()
//        }
        loginFb.setReadPermissions("email")
        loginFb.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(error: FacebookException?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSuccess(loginResult: LoginResult) {
                setResult(RESULT_OK)
                val accessToken = loginResult.accessToken.token
                Log.i("accessToken", accessToken)
                val client = RetrofitClient().authentificationApi.sendKeysFacebook(accessToken)

                Log.i("Request", client.request().toString())
                client.enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        Log.i("Error", t!!.message)
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        Log.i("Response header", response!!.headers().toString())
                        Log.i("Response body", response.body())
                        if (response.isSuccessful) {

                        }
                    }
                })
            }
        })
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, "Une faible connection internet ", Toast.LENGTH_SHORT).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == authentification!!.REQ_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            this.authentification!!.handleResult(result)
        }
    }
}
