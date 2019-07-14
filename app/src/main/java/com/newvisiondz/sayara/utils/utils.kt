package com.newvisiondz.sayara.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.model.Token
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.views.fragments.Brands
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response

fun getUserToken(userInfo: SharedPreferences): String? {
    return userInfo.getString("token", "Not Found")
}

fun getUserInfo(userInfo: SharedPreferences): Array<String> {
    val userTmp = arrayOf("", "", "", "", "")
    userTmp[0] = userInfo.getString("userlastname", "Not Found")!!
    userTmp[1] = userInfo.getString("userimg", "Not Found")!!
    userTmp[2] = userInfo.getString("useremail", "Not Found")!!
    userTmp[3] = userInfo.getString("userfirstname", "Not Found")!!
    userTmp[4] = userInfo.getString("type", "Not Found")!!
    return userTmp
}

fun setUserPrefrences(userInfo: SharedPreferences, account: Token, acc: GoogleSignInAccount) {
    val editor = userInfo.edit()
    editor.putString("useremail", account.email)
    editor.putString("userimg", acc.photoUrl.toString())
    editor.putString("userlastname", acc.displayName)
    editor.putString("userfirstname", acc.familyName)
    editor.putString("token", account.token)
    editor.putString("type", "google")
    editor.apply()
}

fun setUserPrefrences(userInfo: SharedPreferences, account: Token, jsonObject: JSONObject) {
    val editor = userInfo.edit()
    editor.putString("useremail", account.email)
    editor.putString("userimg", jsonObject.getString("id"))
    editor.putString("userlastname", jsonObject.getString("name"))
    editor.putString("token", account.token)
    editor.putString("type", "facebook")
    editor.apply()
}

fun clearUserInfo(userInfo: SharedPreferences) {
    userInfo.edit().clear().apply()
}

fun isOnline(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val nt = cm!!.activeNetworkInfo

    return (nt != null && nt.isConnected)
}

fun updateNotificationTokenWithToken(token: String, context: Context, userInfo: SharedPreferences) {
    val jsonObject = JsonObject()
    jsonObject.addProperty("token", token)
    val call = RetrofitClient(context).serverDataApi
        .updateUser(getUserToken(userInfo)!!, jsonObject)
    Log.i("FirebaseServiceRes", "sending Token")
    call.enqueue(object : Callback<JsonObject> {
        override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {
            t.printStackTrace()
        }

        override fun onResponse(call: retrofit2.Call<JsonObject>, response: Response<JsonObject>) {
            if (response.isSuccessful) {
                Log.i("FirebaseServiceRes", response.body().toString())
            }
        }

    })
}

fun updateNotificationToken(context: Context) {
    FirebaseInstanceId.getInstance().instanceId
        .addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(Brands.TAG, "getInstanceId failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result?.token
            updateNotificationTokenWithToken(
                token!!,
                context,
                context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
            )
            Log.i("FirebaseServiceRes", token)
            // Log and toast
            val msg = context.getString(R.string.msg_token_fmt, token)
            Log.d(Brands.TAG, msg)
        }
}