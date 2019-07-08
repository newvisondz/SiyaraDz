package com.newvisiondz.sayara.services.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.newvisiondz.sayara.model.Token
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.setUserPrefrences
import com.newvisiondz.sayara.views.MainActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FacebookAuthentification(var context: Context, val auth: FirebaseAuth) {

    private var userInfo: SharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)

    fun signIn(loginResult: LoginResult) {
        val accessToken = loginResult.accessToken.token
        val client = RetrofitClient(context).authentificationApi.sendKeysFacebook(accessToken)
        lateinit var jsonResponseObject: JSONObject
        val request = GraphRequest.newMeRequest(loginResult.accessToken) { objet, _ ->
            jsonResponseObject = objet
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name")
        request.parameters = parameters
        request.executeAsync()

        val runnable = Runnable {
            client.enqueue(object : Callback<Token> {
                override fun onFailure(call: Call<Token>?, t: Throwable?) {
                }

                override fun onResponse(call: Call<Token>?, response: Response<Token>?) {
                    if (response!!.isSuccessful) {
                        setUserPrefrences(userInfo, response.body()!!, jsonResponseObject)
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        (context as Activity).finish()
                    }
                }
            })
        }
        val handler = Handler()
        handler.postDelayed(runnable, 3000)
    }

    fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("FacebookLog", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener(context as Activity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("FacebookLog", "signInWithCredential:success")
                val user = auth.currentUser
//                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w("FacebookLog", "signInWithCredential:failure", task.exception)
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
//                    updateUI(null)//
            }
        }

    }
}