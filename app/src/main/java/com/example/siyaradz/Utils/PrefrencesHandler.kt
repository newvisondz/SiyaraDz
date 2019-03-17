package com.example.siyaradz.Utils

import android.content.SharedPreferences
import com.example.siyaradz.Tokens.GoogleToken

class PrefrencesHandler {
    fun getUserToken(userInfo: SharedPreferences): String? {
        return userInfo.getString("token", "Not Found")
    }

    fun setUserPrefrences(userInfo: SharedPreferences, account: GoogleToken) {
        val editor = userInfo.edit()
        editor.putString("username", account.email)
        editor.putString("token", account.token)
        editor.apply()
    }

    fun removeUserToken(userInfo: SharedPreferences) {
        val editor = userInfo.edit()
        editor.remove("token")
        editor.apply()
    }
}