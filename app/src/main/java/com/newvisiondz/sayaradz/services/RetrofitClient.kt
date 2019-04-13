package com.newvisiondz.sayaradz.services

import android.app.Application
import android.content.Context
import com.newvisiondz.sayaradz.services.Auth.AuthentificationApi
import com.google.gson.GsonBuilder
import com.newvisiondz.sayaradz.views.MainActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Modifier
import java.util.concurrent.TimeUnit
import okhttp3.Cache
import java.io.File


class RetrofitClient (var context:Context){
    private val retrofit: Retrofit
    val DISK_CACHE_SIZE = 10 * 1024 * 1024
    private val BASE_URL = "http://sayaradz-sayaradz.7e14.starter-us-west-2.openshiftapps.com/"

    val authentificationApi: AuthentificationApi
        get() = retrofit.create(AuthentificationApi::class.java)

    val serverDataApi: ServerDataApi
        get() = retrofit.create(ServerDataApi::class.java)

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val gson =
            GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
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
