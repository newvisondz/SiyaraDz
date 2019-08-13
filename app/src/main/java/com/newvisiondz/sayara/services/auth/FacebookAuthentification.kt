package com.newvisiondz.sayara.services.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.newvisiondz.sayara.model.Token
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.setUserPrefrences
import com.newvisiondz.sayara.utils.updateNotificationToken
import com.newvisiondz.sayara.views.MainActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FacebookAuthentification(var context: Context, val auth: FirebaseAuth) {

    private var userInfo: SharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)

    fun signIn(accessToken: AccessToken) {
        val client = RetrofitClient(context).authentificationApi.sendKeysFacebook(accessToken.token)
        lateinit var jsonResponseObject: JSONObject
        val request = GraphRequest.newMeRequest(accessToken) { objet, _ ->
            jsonResponseObject = objet
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name")
        request.parameters = parameters
        request.executeAsync()
        client.enqueue(object : Callback<Token> {
            override fun onFailure(call: Call<Token>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<Token>?, response: Response<Token>?) {
                if (response!!.isSuccessful) {
                    setUserPrefrences(userInfo, response.body()!!, jsonResponseObject)
                    val intent = Intent(context, MainActivity::class.java)
                    updateNotificationToken(context)
                    context.startActivity(intent)
                    (context as Activity).finish()
                }
            }
        })

    }

    fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener(context as Activity) { task ->
            if (task.isSuccessful) {
                Log.d("FacebookLog", "signInWithCredential:success")
                signIn(token)
            } else {
                Toast.makeText(context, "Authentication failed. May be you signed in with a google account !", Toast.LENGTH_SHORT).show()
            }
        }

    }
}