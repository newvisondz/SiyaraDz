package com.example.siyaradz

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface AuthentificationApi {
    @GET("auth/google/callback")
    fun SendKeys(
        @Query("code") token: String,
        @Query("scope") scope: String
    ): Call<ResponseBody>
}

