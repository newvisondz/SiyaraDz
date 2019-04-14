package com.newvisiondz.sayaradz.services.Auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import com.facebook.GraphRequest
import com.newvisiondz.sayaradz.services.RetrofitClient
import com.facebook.login.LoginResult
import com.google.gson.JsonObject
import com.newvisiondz.sayaradz.Tokens.Token
import com.newvisiondz.sayaradz.Utils.MessagesUtils
import com.newvisiondz.sayaradz.Utils.PrefrencesHandler
import com.newvisiondz.sayaradz.views.MainActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FacebookAuthentification(var context: Context) {
    private var messages =MessagesUtils()
    private var userInfo: SharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    private var prefrencesHandler= PrefrencesHandler()

    fun signIn(loginResult: LoginResult, view: View) {
        val accessToken = loginResult.accessToken.token
        lateinit var jsonResponseObject:JSONObject
        val request = GraphRequest.newMeRequest(loginResult.accessToken) { objet, response ->
            jsonResponseObject=objet
            Log.i("main",objet.getString("id")+objet.getString("name"))
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name")
        request.parameters = parameters
        request.executeAsync()
        Thread.sleep(3000)//sometimes one async task is faster than the other thus, we'll have to wait
        val client = RetrofitClient(context).authentificationApi.sendKeysFacebook(accessToken)
        client.enqueue(object : Callback<Token> {
            override fun onFailure(call: Call<Token>?, t: Throwable?) {
                messages.displaySnackBar(view, "Error try agin later")
            }

            override fun onResponse(call: Call<Token>?, response: Response<Token>?) {
                if (response!!.isSuccessful) {
                    prefrencesHandler.setUserPrefrences(userInfo,response.body()!!, jsonResponseObject)
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
            }
        })
    }
}