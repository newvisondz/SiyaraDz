package com.example.siyaradz.views

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.siyaradz.R
import com.example.siyaradz.services.GoogleAuthentification
import com.example.siyaradz.services.RetrofitClient
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private var callbackManager = CallbackManager.Factory.create()!!

    private var authentification: GoogleAuthentification? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.APP_STATE))
            .requestServerAuthCode(getString(R.string.server_client_id))
            .requestEmail()
            .build()

        authentification = GoogleAuthentification(this, signInOptions)


        authentification!!.getuserName().visibility = View.GONE


        authentification!!.signInButton.setOnClickListener {
            authentification!!.signIn(this)
        }
        authentification!!.signOutButton.setOnClickListener {
            authentification!!.signOut()
        }


        var loginButton = findViewById<LoginButton>(R.id.login_button)

        loginButton.authType = "rerequest"
        loginButton.setReadPermissions(Arrays.asList("email", "user_posts"));


        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                setResult(RESULT_OK)
                val accessToken = loginResult.accessToken.token

                Log.i("accessToken", accessToken)
                val call = RetrofitClient()
                    .authentificationApi
                    .sendKeysFacebook(accessToken)

                call.enqueue(object : Callback<String> {

                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        Log.i("Token", t!!.message)
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        if (response!!.isSuccessful) {
                            Log.i("Response", response.body())
                        }
                    }
                })
            }

            override fun onCancel() {
                println("canceled")
            }

            override fun onError(exception: FacebookException) {
                println("error")
            }
        })
    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, "Une faible connection internet ", Toast.LENGTH_SHORT)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == authentification!!.REQ_CODE) {
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            this.authentification!!.handleResult(result)
        }
    }
}
