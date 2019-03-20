package com.newvisiondz.sayaradz.services.Auth

import com.newvisiondz.sayaradz.services.RetrofitClient
import com.facebook.login.LoginResult

class FacebookAuthentification {
    public fun signIn(loginResult: LoginResult) {
        if (loginResult.accessToken != null) "Logged in" else "Login error"

        val call = RetrofitClient()
            .authentificationApi
            .sendKeysFacebook(loginResult.accessToken.token)

    }
}