package com.newvisiondz.sayara.services

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.newvisiondz.sayara.model.Command
import com.newvisiondz.sayara.model.Model
import com.newvisiondz.sayara.model.UsedCar
import com.newvisiondz.sayara.model.Version
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ServerDataApi {


    @GET("manufacturers/")
    fun getAllBrands(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("perpage") perPage: Int,
        @Query("q") partilStrnig: String
    ): Call<JsonElement>

    @GET("manufacturers/")
    fun filterBrands(
        @Header("Authorization") token: String,
        @Query("q") partilStrnig: String
    ): Call<JsonElement>


    @PUT("autom/me")
    fun updateUser(
        @Header("Authorization") token: String,
        @Body body: JsonObject
    ): Call<JsonObject>

    @GET("manufacturers/{manufacturerName}/models/")
    fun getAllModels(
        @Header("Authorization") token: String,
        @Path("manufacturerName") manufacturerName: String,
        @Query("page") page: Int,
        @Query("perpage") perPage: Int
    ): Call<JsonElement>

    @GET("manufacturers/{manufacturerId}/models")
    fun getAllModels(
        @Header("Authorization") token: String,
        @Path("manufacturerId") manufacturerName: String,
        @Query("q") q: String
    ): Call<JsonElement>

    @GET("manufacturers/{manufacturerId}/models/{modelId}")
    fun getModelDetails(
        @Header("Authorization") token: String,
        @Path("manufacturerId") manufacturerName: String,
        @Path("modelId") modelId: String
    ): Call<Model>

    @GET("manufacturers/{manufacturerId}/models/{modelId}/versions")
    fun getAllVersion(
        @Header("Authorization") token: String,
        @Path("manufacturerId") manufacturerName: String,
        @Path("modelId") modelId: String
    ): Call<JsonArray>

    @GET("manufacturers/{manufacturerId}/models/{modelId}/versions/{versionId}")
    fun getVersionDetails(
        @Header("Authorization") token: String,
        @Path("manufacturerId") manufacturerName: String,
        @Path("modelId") modelId: String,
        @Path("versionId") versionId: String
    ): Call<Version>

    @GET("/manufacturers/{manufacturerId}/models/{modelId}/versions/{versionId}/vehicles/check")
    fun sendUserCommand(
        @Header("Authorization") token: String,
        @Path("manufacturerId") manufacturerName: String,
        @Path("modelId") modelId: String,
        @Path("versionId") versionId: String,
        @Query("options[]") options: MutableList<String>
    ): Call<Command>

    @POST("commands/")
    fun confirmUserCommande(
        @Header("Authorization") token: String,
        @Body body: JsonObject
    ): Call<JsonObject>

    @GET("manufacturers/{manufacturer_id}/models/{model_id}/versions/{version_id}/vehicles/{vehicle_id}")
    fun getDataSheetCar(
        @Header("Authorization") token: String,
        @Path("manufacturer_id") manufacturerName: String,
        @Path("modelId") modelId: String,
        @Path("version_id") version_id: String
    ): Call<JsonElement>

    @GET("used-cars")
    fun getAllUsedCars(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("perpage") perPage: Int
    ): Call<List<UsedCar>>

    @DELETE("used-cars/{usedCarId}")
    fun deleteUsedCar(
        @Header("Authorization") token: String,
        @Path("usedCarId") usedCarId: String
    ): Call<JsonObject>

    @Multipart
    @POST("used-cars")
    fun createUsedCar(
        @Header("Authorization") token: String,
        @Part images: List<MultipartBody.Part>,
        @Part("manufacturerId") manufacturerId: String,
        @Part("modelId") modelId: String,
        @Part("versionId") versionId: String,
        @Part("registrationDate") registrationDate: String,
        @Part("currentMiles") currentMiles: Double,
        @Part("minPrice") minPrice: Double,
        @Part("color") color: String    ,
        @Part("title") title: String,
        @Part("manufacturer") manufacturer: String,
        @Part("model") model: String,
        @Part("version") version: String
    ): Call<UsedCar>

    @GET("manufacturers")
    fun getAdditionalInfo(
        @Header("Authorization") token: String,
        @Query("fields") fields: String
    ): Call<JsonElement>

    @GET("used-cars/{usedCarId}/bids")
    fun getAllBidsOfUsedCar(
        @Header("Authorization") token: String,
        @Query("fields") fields: String = "creator",
        @Path("usedCarId") usedCarId: String
    ):Call<JsonElement>
}
