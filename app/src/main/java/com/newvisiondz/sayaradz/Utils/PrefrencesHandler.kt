package com.newvisiondz.sayaradz.Utils

import android.content.SharedPreferences
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.newvisiondz.sayaradz.Tokens.Token
import org.json.JSONObject

class PrefrencesHandler {
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

    fun removeUserToken(userInfo: SharedPreferences) {
        val editor = userInfo.edit()
        editor.remove("token")
        editor.apply()
    }

    fun clearUserInfo(userInfo: SharedPreferences) {
        userInfo.edit().clear().apply()
    }
}