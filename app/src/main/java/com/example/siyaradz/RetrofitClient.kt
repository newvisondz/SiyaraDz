package com.example.siyaradz

import android.util.Base64
import com.google.android.gms.common.api.Api
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request

import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.io.IOException
import java.util.logging.Level
import okhttp3.logging.HttpLoggingInterceptor



class RetrofitClient private constructor() {
    private val retrofit: Retrofit

    val api: AuthentificationApi
        get() = retrofit.create(AuthentificationApi::class.java)


    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    companion object {

        private const val BASE_URL = "https://sayara-dz.herokuapp.com/"
        private var mInstance: RetrofitClient? = null

        val instance: RetrofitClient
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = RetrofitClient()
                }
                return mInstance!!
            }
    }
}
