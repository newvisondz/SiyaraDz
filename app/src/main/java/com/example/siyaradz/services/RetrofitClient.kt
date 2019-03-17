package com.example.siyaradz.services

import com.example.siyaradz.services.Auth.AuthentificationApi
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient {
    private val retrofit: Retrofit
    private val BASE_URL = "https://sayara-dz.herokuapp.com/"

    val authentificationApi: AuthentificationApi
        get() = retrofit.create(AuthentificationApi::class.java)

    val serverDataApi: ServerDataApi
        get() = retrofit.create(ServerDataApi::class.java)


    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val gson = GsonBuilder().setLenient().create()

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)

            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)

            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

}
