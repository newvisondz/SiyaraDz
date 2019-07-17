package com.newvisiondz.sayara.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.model.*
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.views.fragments.Brands
import com.newvisiondz.sayara.views.fragments.Versions
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

fun getUserToken(userInfo: SharedPreferences): String? {
    return userInfo.getString("token", "Not Found")
}

fun getUserInfo(userInfo: SharedPreferences): Array<String> {
    val userTmp = arrayOf("", "", "", "", "", "", "")
    userTmp[0] = userInfo.getString("userlastname", "Not Found")!!
    userTmp[1] = userInfo.getString("userimg", "Not Found")!!
    userTmp[2] = userInfo.getString("useremail", "Not Found")!!
    userTmp[3] = userInfo.getString("userfirstname", "Not Found")!!
    userTmp[4] = userInfo.getString("type", "Not Found")!!
    userTmp[5] = userInfo.getString("birthDate", "Not Found")!!
    userTmp[6] = userInfo.getString("address", "Not Found")!!
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

fun udpateUserPrefrences(
    userInfo: SharedPreferences,
    firstName: String,
    lastName: String,
    address: String,
    phone: String,
    birthDate: String
) {
    val editor = userInfo.edit()
    editor.putString("", firstName)
    editor.putString("", lastName)
    editor.putString("", birthDate)
    editor.putString("", address)
    editor.putString("", phone)
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

fun setUserAdditionalInfo(userInfo: SharedPreferences) {

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

fun optionsMapping(list1: Version, list2: Version): MutableList<VersionCompare> {
    val listRes = mutableListOf<VersionCompare>()
    var itemRes: VersionCompare
    for (item in list1.options) {
        itemRes = VersionCompare()
        when (item.name) {
            "places" -> {
                itemRes.optionName = "places"
                itemRes.firstValue = getOptionListAsString(item.values)
                itemRes.secondValue = getOptionWithName(list2, "places")?.values?.let { getOptionListAsString(it) }!!
            }
            "Type du carburant" -> {
                itemRes.optionName = "Type du carburant"
                itemRes.firstValue = getOptionListAsString(item.values)
                itemRes.secondValue =
                    getOptionWithName(list2, "Type du carburant")?.values?.let { getOptionListAsString(it) }!!
            }
            "moteur" -> {
                itemRes.optionName = "moteur"
                itemRes.firstValue = getOptionListAsString(item.values)
                itemRes.secondValue = getOptionWithName(list2, "moteur")?.values?.let { getOptionListAsString(it) }!!
            }
            "Boite" -> {
                itemRes.optionName = "Boite"
                itemRes.firstValue = getOptionListAsString(item.values)
                itemRes.secondValue = getOptionWithName(list2, "Boite")?.values?.let { getOptionListAsString(it) }!!
            }
        }
        listRes.add(itemRes)
    }
    return listRes
}

fun getOptionListAsString(options: MutableList<Value>): String {
    val result = StringBuilder()
    for (item in options) {
        result.append("${item.value},")
    }
    Log.i("compare method", result.toString())
    return result.toString()
}

fun getOptionWithName(version: Version, name: String): Option? {
    for (item in version.options) {
        if (item.name == name) return item
    }
    return null
}