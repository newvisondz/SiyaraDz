package com.newvisiondz.sayaradz.services

import com.google.gson.JsonElement
import com.newvisiondz.sayaradz.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ServerDataApi {
    @GET("manufacturers/")
    fun getAllBrands(
        @Header("Authorization") token: String,
        @Query("page") page: String,
        @Query("perpage") perPage: String
    ): Call<JsonElement>

    @GET("{imgId}")
    fun getBrandImage(@Path("imgId", encoded = true) imgName: String): Call<ResponseBody>


    @PUT("autom/me")
    fun updateUser(
        @Header("Authorization") token: String,
        @Body body: String
    ): Call<ResponseBody>


    @GET("manufacturers/{manufacturerName}/models/")
    fun getAllModels(
        @Header("Authorization") token: String,
        @Path("manufacturerName") manufacturerName:String,
        @Query("page") page: String,
        @Query("perpage") perPage: String
    ): Call<JsonElement>

    @GET("manufacturers/{manufacturerName}/models/")
    fun getAllModels(
        @Header("Authorization") token: String,
        @Path("manufacturerName") manufacturerName:String
    ): Call<JsonElement>
}
