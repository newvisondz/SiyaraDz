package com.example.siyaradz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient

class GoogleAuthentification {
    private var googleClient: GoogleApiClient? = null
    public val REQ_CODE: Int = 9001
    private var userName: TextView
    internal  var signInButton: SignInButton
    internal  var signOutButton : Button

    constructor(context: Context,signInOptions:GoogleSignInOptions) {
        context as Activity
        this.googleClient = GoogleApiClient.Builder(context).enableAutoManage(context as FragmentActivity,context as GoogleApiClient.OnConnectionFailedListener).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build()
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
    public fun handleResult(result: GoogleSignInResult){
        if (result.isSuccess){
            var account =result.signInAccount
            var name = account?.displayName
            var email =account?.email
            userName.text = email
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
}