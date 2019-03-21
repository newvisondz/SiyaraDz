package com.newvisiondz.sayaradz.services.Auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.Button
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.Tokens.GoogleToken
import com.newvisiondz.sayaradz.Utils.PrefrencesHandler
import com.newvisiondz.sayaradz.services.RetrofitClient
import com.newvisiondz.sayaradz.views.MainActivity
import com.newvisiondz.sayaradz.views.NavigationActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GoogleAuthentification {
    private var googleClient: GoogleApiClient? = null
    val REQ_CODE: Int = 9001
    private var context: Context
    internal var signInButton: Button
    // internal var signOutButton: Button
    private var userInfo: SharedPreferences
    private var prefrencesHandler: PrefrencesHandler


    constructor(context: Context, signInOptions: GoogleSignInOptions) {
        context as Activity
        this.googleClient = GoogleApiClient.Builder(context)
            .enableAutoManage(context as FragmentActivity, context as GoogleApiClient.OnConnectionFailedListener)
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
            .build()

        this.signInButton = context.findViewById(R.id.loging)
        //this.signOutButton = context.findViewById(R.id.googleSignOut)
        this.context = context
        userInfo = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        prefrencesHandler = PrefrencesHandler()
    }


    fun signIn(context: Context) {
        var intent: Intent = Auth.GoogleSignInApi.getSignInIntent(googleClient)
        context as Activity
        context.startActivityForResult(intent, REQ_CODE)
    }

    public fun signOut() {
        Auth.GoogleSignInApi.signOut(googleClient).setResultCallback {
            //updateUi(false)
        }
        prefrencesHandler.removeUserToken(userInfo)
    }

    public fun handleResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            val account = result.signInAccount
            Log.i("sendreq","Google")
            if (prefrencesHandler.getUserToken(userInfo).equals("Not Found")) {
                val call = RetrofitClient()
                    .authentificationApi
                    .sendKeysGoogle(account!!.serverAuthCode!!, account.requestedScopes.toString())

                call.enqueue(object : Callback<GoogleToken> {
                    override fun onFailure(call: Call<GoogleToken>?, t: Throwable?) {
                        t!!.printStackTrace()
                    }

                    override fun onResponse(call: Call<GoogleToken>?, response: Response<GoogleToken>?) {
                        if (response!!.isSuccessful) {
                            prefrencesHandler.setUserPrefrences(userInfo, response.body()!!)
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                })
            } else {
                val intent = Intent(context, NavigationActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}