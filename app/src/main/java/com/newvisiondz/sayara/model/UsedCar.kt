package com.newvisiondz.sayara.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "usedCar")
data class UsedCar(
    @PrimaryKey
    var id: String = "",
    @SerializedName("currrentMiles")
    var currrentMiles: Double = 0.0,
    @SerializedName("manufacturer")
    var carBrandId: String = "",
    @SerializedName("minPrice")
    var price: Double = 0.0,
    @SerializedName("registrationDate")
    var yearOfRegistration: String = "",
    var color: String = "",
    @Expose(serialize = false, deserialize = false)
    @Ignore
    var uris: MutableList<Uri> = mutableListOf(),
    var images: MutableList<String> = mutableListOf("https://drop.ndtv.com/albums/AUTO/2019-bmw-x7-suv/30.jpg"),
    @SerializedName("model")
    var carModel: String = "",
    var version: String = "",
    var owner: String = ""
)   