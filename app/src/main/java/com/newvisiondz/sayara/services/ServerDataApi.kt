package com.newvisiondz.sayara.services

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.newvisiondz.sayara.model.Command
import com.newvisiondz.sayara.model.Model
import com.newvisiondz.sayara.model.Version
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
}
