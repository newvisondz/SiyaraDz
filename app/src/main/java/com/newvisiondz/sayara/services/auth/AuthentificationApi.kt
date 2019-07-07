package com.newvisiondz.sayara.services.auth


import com.newvisiondz.sayara.model.Token
import retrofit2.Call
import retrofit2.http.*

interface AuthentificationApi {
    @GET("auth/google/callback")
    fun sendKeysGoogle(
        @Query("code") token: String,
        @Query("scope") scope: String
    ): Call<Token>

    @GET("auth/facebook/")
    fun sendKeysFacebook(
        @Query("access_token") token: String
    ): Call<Token>
}

