package com.newvisiondz.sayaradz.services

import android.content.Context
import com.google.gson.GsonBuilder
import com.newvisiondz.sayaradz.services.Auth.AuthentificationApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitClient (var context:Context){
    private val retrofit: Retrofit
    private val BASE_URL = "http://sayaradz-sayaradz-2.7e14.starter-us-west-2.openshiftapps.com/"

    val authentificationApi: AuthentificationApi
        get() = retrofit.create(AuthentificationApi::class.java)

    val serverDataApi: ServerDataApi
        get() = retrofit.create(ServerDataApi::class.java)

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val gson =
            GsonBuilder()
                .setLenient()
                .create()

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(NetworkConnectionInterceptor(this.context))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

}
