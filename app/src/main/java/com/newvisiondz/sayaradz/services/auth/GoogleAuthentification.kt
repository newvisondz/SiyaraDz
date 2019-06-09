package com.newvisiondz.sayaradz.services.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.tokens.Token
import com.newvisiondz.sayaradz.services.RetrofitClient
import com.newvisiondz.sayaradz.utils.setUserPrefrences
import com.newvisiondz.sayaradz.views.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//this.signOutButton = context.findViewById(R.id.googleSignOut)

class GoogleAuthentification (private var context: Context) {

    companion object {
        const val REQ_CODE: Int = 9001
    }
    private var googleClient: GoogleApiClient? = null
//    internal var signInButton: Button
    private var userInfo: SharedPreferences


    init {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.APP_STATE))
            .requestServerAuthCode(context.getString(R.string.server_client_id))
            .requestEmail()
            .build()
//        context as Activity
        this.googleClient = GoogleApiClient.Builder(context)
            .enableAutoManage(
                context as FragmentActivity,
                context as GoogleApiClient.OnConnectionFailedListener
            )
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
            .build()
//        this.signInButton = (context as Activity).loging
        userInfo = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    }


    fun signIn(context: Context) {
        val intent: Intent = Auth.GoogleSignInApi.getSignInIntent(googleClient)
        context as Activity
        context.startActivityForResult(intent, REQ_CODE)
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
                        setUserPrefrences(userInfo, response.body()!!, account)
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        (context as Activity).finish()
                    }
                }
            })

        }

    }
}