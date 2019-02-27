package com.example.siyaradz.Services

import com.example.siyaradz.Tokens.GoogleToken
import okhttp3.ResponseBody
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
        @Query("code") token: String
    ): Call<String>
}

