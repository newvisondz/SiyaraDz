package com.example.siyaradz.services.Auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.siyaradz.R
import com.example.siyaradz.Tokens.GoogleToken
import com.example.siyaradz.services.RetrofitClient
import com.example.siyaradz.views.BrandDisplay
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GoogleAuthentification {
    private var googleClient: GoogleApiClient? = null
    val REQ_CODE: Int = 9001
    private var userName: TextView
    private var context: Context
    internal var signInButton: SignInButton
    internal var signOutButton: Button


    constructor(context: Context, signInOptions: GoogleSignInOptions) {
        context as Activity
        this.googleClient = GoogleApiClient.Builder(context)
            .enableAutoManage(context as FragmentActivity, context as GoogleApiClient.OnConnectionFailedListener)
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
            .build()
        this.userName = context.findViewById(R.id.userName)
        this.signInButton = context.findViewById(R.id.googleSignIn)
        this.signOutButton = context.findViewById(R.id.googleSignOut)
        this.context = context
    }

    fun getuserName(): TextView {
        return this.userName
    }


    public fun signIn(context: Context) {
        var intent: Intent = Auth.GoogleSignInApi.getSignInIntent(googleClient)
        context as Activity
        context.startActivityForResult(intent, REQ_CODE)
    }

    public fun signOut() {
        Auth.GoogleSignInApi.signOut(googleClient).setResultCallback {
            updateUi(false)
        }
    }

    public fun handleResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            var account = result.signInAccount
            var email = account?.email

            userName.text = email

            val call = RetrofitClient()
                .authentificationApi
                .sendKeysGoogle(account!!.serverAuthCode!!, account.requestedScopes.toString())

            call.enqueue(object : Callback<GoogleToken> {
                override fun onFailure(call: Call<GoogleToken>?, t: Throwable?) {
                    t!!.printStackTrace()
                }

                override fun onResponse(call: Call<GoogleToken>?, response: Response<GoogleToken>?) {
                    if (response!!.isSuccessful && response.body() != null) {
                        val intent = Intent(context, BrandDisplay::class.java)
                        context.startActivity(intent)
                    } else {
                        Log.i("Response null", response.body().toString())
                    }
                }
            })
            updateUi(true)
        } else {
            updateUi(false)
        }
    }

    public fun updateUi(isLogedIn: Boolean) {
        if (isLogedIn) {
            userName.visibility = View.VISIBLE
            signInButton.visibility = View.GONE

        } else {
            userName.visibility = View.GONE
            signInButton.visibility = View.VISIBLE
        }
    }
}