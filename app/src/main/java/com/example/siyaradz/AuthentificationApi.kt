package com.example.siyaradz

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface AuthentificationApi {
    @GET("auth/facebook/callback")
    fun SendKeysFacebook(
        @Query("code") token: String
    ): Call<ResponseBody>
}

