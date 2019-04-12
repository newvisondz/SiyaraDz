package com.newvisiondz.sayaradz.services

import com.newvisiondz.sayaradz.services.Auth.AuthentificationApi
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Modifier


class RetrofitClient {
    private val retrofit: Retrofit
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
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

}
