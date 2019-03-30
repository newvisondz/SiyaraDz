package com.newvisiondz.sayaradz.Utils

import android.content.SharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.newvisiondz.sayaradz.Tokens.GoogleToken

class PrefrencesHandler {
    fun getUserToken(userInfo: SharedPreferences): String? {
        return userInfo.getString("token", "Not Found")
    }

    fun getUserInfo(userInfo: SharedPreferences): Array<String> {
        val userTmp = arrayOf("", "", "","")
        userTmp[0] = userInfo.getString("userlastname", "Not Found")!!
        userTmp[1] = userInfo.getString("userimg", "Not Found")!!
        userTmp[2] = userInfo.getString("useremail", "Not Found")!!
        userTmp[3] = userInfo.getString("userfirstname", "Not Found")!!
        return userTmp
    }

    fun setUserPrefrences(userInfo: SharedPreferences, account: GoogleToken, acc: GoogleSignInAccount) {
        val editor = userInfo.edit()
        editor.putString("useremail", account.email)
        editor.putString("userimg", acc.photoUrl.toString())
        editor.putString("userlastname", acc.familyName)
        editor.putString("userfirstname", acc.givenName)
        editor.putString("token", account.token)
        editor.apply()
    }

    fun removeUserToken(userInfo: SharedPreferences) {
        val editor = userInfo.edit()
        editor.remove("token")
        editor.apply()
    }
}