package com.newvisiondz.sayara.services.auth


import com.newvisiondz.sayara.model.Token
import retrofit2.Call
import retrofit2.http.*

interface AuthentificationApi {

    @GET("auth/facebook/")
    fun sendKeysFacebook(
        @Query("access_token") token: String
    ): Call<Token>

    @GET("auth/firebase")
    fun sendKeyFirebase(
        @Query("token") token: String
    ):Call<Token>
}

