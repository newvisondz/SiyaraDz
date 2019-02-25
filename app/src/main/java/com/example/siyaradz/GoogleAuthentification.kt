package com.example.siyaradz

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import retrofit2.Call
import retrofit2.Retrofit
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task


class GoogleAuthentification {
    private var googleClient: GoogleApiClient? = null
    public val REQ_CODE: Int = 9001
    private var userName: TextView
    internal  var signInButton: SignInButton
    internal  var signOutButton : Button



    constructor(context: Context,signInOptions:GoogleSignInOptions) {
        context as Activity
        this.googleClient = GoogleApiClient.Builder(context).enableAutoManage(context as FragmentActivity,context as GoogleApiClient.OnConnectionFailedListener)
            .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions)
            .build()
        this.userName = context.findViewById(R.id.userName)
        this.signInButton = context.findViewById(R.id.googleSignIn)
        this.signOutButton = context.findViewById(R.id.googleSignOut)
    }

    fun getgoogleClient(): GoogleApiClient? {
        return this.googleClient
    }
    fun getuserName():TextView {
        return this.userName
    }


    public fun signIn(context: Context) {
        var intent: Intent = Auth.GoogleSignInApi.getSignInIntent(googleClient)
        context as Activity
        context.startActivityForResult(intent,REQ_CODE)
    }

    public fun signOut() {
        Auth.GoogleSignInApi.signOut(googleClient).setResultCallback {
            updateUi(false)
        }
    }

    public fun handleResult(result: GoogleSignInResult,context:Context){
        if (result.isSuccess){
            var account =result.signInAccount
            var name = account?.displayName
            var email =account?.email

            var scopes =account!!.requestedScopes

            //var tab:String=scopes.spliterator()
            for (scope in scopes) {
                Log.i("scope",scope.scopeUri)
            }
            userName.text = email
            Log.i("token","${account.serverAuthCode}")

            val call = RetrofitClient
                .instance
                .api
                .SendKeys(account.serverAuthCode!!,account.requestedScopes.toString())

            call.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    Log.i("Token",t!!.message)
                }
                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response!!.isSuccessful) {
                        Log.i("Response", response.body().toString())
                    }else {
                        Log.i("Response null", response.body().toString())
                    }
                }
            })

            updateUi(true)
        }else{
            updateUi(false)
        }
    }

    public fun  updateUi(isLogedIn:Boolean) {
        if (isLogedIn) {
            userName.visibility= View.VISIBLE
            signInButton.visibility= View.GONE

        }else {
            userName.visibility= View.GONE
            signInButton.visibility= View.VISIBLE
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account!!.idToken

            // TODO(developer): send ID Token to server and validate

            updateUi(true)
        } catch (e: ApiException) {
            Log.w(TAG, "handleSignInResult:error", e)
            updateUi(false)
        }

    }
}