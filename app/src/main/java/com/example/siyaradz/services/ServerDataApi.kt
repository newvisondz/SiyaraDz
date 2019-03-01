package com.example.siyaradz.services

import com.example.siyaradz.model.Marque
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerDataApi {
    @GET("fabricant/model")
    fun GetAllBrands(
        @Query("sort") sort: String,
        @Query("perpage") perpage: String,
        @Query("select") select: String,
        @Query("page") page: String,
        @Query("marque") marque: String
    ): Call<List<Marque>>

    @GET("fabricant/model")
    fun GetAllBrands(
        @Query("sort") sort: String,
        @Query("select") select: String,
        @Query("page") page: String
    ): Call<List<Marque>>
}
