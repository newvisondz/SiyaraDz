package com.newvisiondz.sayara.services.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.model.Token
import com.newvisiondz.sayara.screens.entryScreens.MainActivity
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.setUserPrefrences
import com.newvisiondz.sayara.utils.subscribe
import com.newvisiondz.sayara.utils.updateNotificationToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GoogleAuthentification(private var context: Context, private var auth: FirebaseAuth) {

    companion object {
        const val REQ_CODE: Int = 9001
        private const val TAG: String = "GOOGLE"
    }


    private var googleSignInClient: GoogleSignInClient

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


    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount, context: Context) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(context as Activity) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "signInWithCredential:success")
                val user: FirebaseUser? = auth.currentUser
                user!!.getIdToken(true)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val idToken = task.result?.token
                            idToken?.let {
                                RetrofitClient(context).authentificationApi.sendKeyFirebase(it)
                                    .enqueue(
                                        object : Callback<Token> {
                                            override fun onFailure(
                                                call: Call<Token>,
                                                t: Throwable
                                            ) {
                                            }

                                            override fun onResponse(
                                                call: Call<Token>,
                                                response: Response<Token>
                                            ) {
                                                if (response.isSuccessful) {
                                                    setUserPrefrences(
                                                        userInfo,
                                                        response.body()!!,
                                                        acct
                                                    )
                                                    subscribe(response.body()!!.id, context)
                                                    val intent =
                                                        Intent(context, MainActivity::class.java)
                                                    context.startService(
                                                        Intent(
                                                            context,
                                                            FirebaseMessagingService::class.java
                                                        )
                                                    )
                                                    context.startActivity(intent)
                                                    updateNotificationToken(context)
                                                    context.finish()
                                                }
                                            }

                                        })
                            }
                        } else {
                            Log.w(TAG, "signInWithBackend:failure", task.exception)
                        }
                    }
            } else {
                Log.w(TAG, "signInWithCredential:failure", task.exception)
            }
        }
    }

}