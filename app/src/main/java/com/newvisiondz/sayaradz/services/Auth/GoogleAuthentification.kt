package com.newvisiondz.sayaradz.services.Auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v4.app.FragmentActivity
import android.widget.Button
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.newvisiondz.sayaradz.Tokens.Token
import com.newvisiondz.sayaradz.Utils.PrefrencesHandler
import com.newvisiondz.sayaradz.services.RetrofitClient
import com.newvisiondz.sayaradz.views.MainActivity
import kotlinx.android.synthetic.main.content_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GoogleAuthentification//this.signOutButton = context.findViewById(R.id.googleSignOut)
    (private var context: Context, signInOptions: GoogleSignInOptions) {
    private var googleClient: GoogleApiClient? = null
    val REQ_CODE: Int = 9001
    internal var signInButton: Button
    // internal var signOutButton: Button
    private var userInfo: SharedPreferences
    private var prefrencesHandler: PrefrencesHandler


    init {
        context as Activity
        this.googleClient = GoogleApiClient.Builder(context)
            .enableAutoManage(context as FragmentActivity, context as GoogleApiClient.OnConnectionFailedListener)
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
            .build()
        this.signInButton = (context as Activity).loging
        userInfo = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        prefrencesHandler = PrefrencesHandler()
    }


    fun signIn(context: Context) {
        val intent: Intent = Auth.GoogleSignInApi.getSignInIntent(googleClient)
        context as Activity
        context.startActivityForResult(intent, REQ_CODE)
    }

    fun signOut() {
        Auth.GoogleSignInApi.signOut(googleClient).setResultCallback {
            prefrencesHandler.removeUserToken(userInfo)
        }
    }

    fun handleResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            val account = result.signInAccount
            val call = RetrofitClient(context)
                .authentificationApi
                .sendKeysGoogle(account!!.serverAuthCode!!, account.requestedScopes.toString())
            call.enqueue(object : Callback<Token> {
                override fun onFailure(call: Call<Token>?, t: Throwable?) {
                    t!!.printStackTrace()
                }

                override fun onResponse(call: Call<Token>?, response: Response<Token>?) {
                    if (response!!.isSuccessful) {
                        prefrencesHandler.setUserPrefrences(userInfo, response.body()!!, account)
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        (context as Activity).finish()
                    }
                }
            })

        }

    }
}