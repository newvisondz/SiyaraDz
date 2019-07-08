package com.newvisiondz.sayara.services.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.model.Token
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.setUserPrefrences
import com.newvisiondz.sayara.views.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleAuthentification(private var context: Context, private var auth: FirebaseAuth) {

    companion object {
        const val REQ_CODE: Int = 9001
        private val TAG: String = "GOOGLE"
    }

    private var googleClient: GoogleApiClient? = null


    var googleSignInClient: GoogleSignInClient

    private var userInfo: SharedPreferences


    init {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.APP_STATE))
            .requestIdToken(context.getString(R.string.server_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context as Activity, signInOptions)
        auth = FirebaseAuth.getInstance()
        userInfo = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        (context as Activity).startActivityForResult(signInIntent, REQ_CODE)
    }

    fun handleResult(result: GoogleSignInAccount?) {
        if (result != null) {
            val call = RetrofitClient(context)
                .authentificationApi
                .sendKeysGoogle(result.idToken!!, result.requestedScopes.toString())
            call.enqueue(object : Callback<Token> {
                override fun onFailure(call: Call<Token>?, t: Throwable?) {
                    t!!.printStackTrace()
                }

                override fun onResponse(call: Call<Token>?, response: Response<Token>?) {
                    if (response!!.isSuccessful) {
                        setUserPrefrences(userInfo, response.body()!!, result)
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        (context as Activity).finish()
                    }
                }
            })

        }

    }


    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount, context: Context) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener(context as Activity) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "signInWithCredential:success")
                val user: FirebaseUser? = auth.currentUser
                handleResult(acct)
                Log.d(TAG, "${user?.email}")
            } else {
                Log.w(TAG, "signInWithCredential:failure", task.exception)
            }
        }
    }

}