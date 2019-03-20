package com.newvisiondz.sayaradz.services.Auth

import com.newvisiondz.sayaradz.Tokens.GoogleToken
import retrofit2.Call
import retrofit2.http.*

interface AuthentificationApi {
    @GET("auth/google/callback")
    fun sendKeysGoogle(
        @Query("code") token: String,
        @Query("scope") scope: String
    ): Call<GoogleToken>

    @GET("auth/facebook/callback")
    fun sendKeysFacebook(
        @Query("accessToken") token: String
    ): Call<String>
}

