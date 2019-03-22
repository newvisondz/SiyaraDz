package com.newvisiondz.sayaradz.services

import com.google.gson.JsonElement
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerDataApi {

    @GET("manufacturers/")
    fun getAllBrands(
        @Header("Authorization") token: String,
        @Query("sort") sort: String,
        @Query("select") select: String,
        @Query("page") page: String,
        @Query("perpage") perPage: String
    ): Call<JsonElement>

    @GET("manufacturers/")
    fun getAllBrands(
        @Header("Authorization") token: String,
        @Query("page") page: String,
        @Query("perpage") perPage: String
    ): Call<JsonElement>

    @GET("{imgId}")
    fun getBrandImage(@Path("imgId",encoded=true) imgName: String): Call<ResponseBody>
}
