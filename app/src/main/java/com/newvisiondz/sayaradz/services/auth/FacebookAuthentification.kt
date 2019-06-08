package com.newvisiondz.sayaradz.services.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.newvisiondz.sayaradz.Tokens.Token
import com.newvisiondz.sayaradz.services.RetrofitClient
import com.newvisiondz.sayaradz.utils.setUserPrefrences
import com.newvisiondz.sayaradz.views.MainActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FacebookAuthentification(var context: Context) {

    private var userInfo: SharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)

    fun signIn(loginResult: LoginResult, view: View) {
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
                    if (response!!.isSuccessful) {setUserPrefrences(userInfo, response.body()!!, jsonResponseObject)
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

}