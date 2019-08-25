package com.newvisiondz.sayara.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "usedCar")
data class UsedCar(
    @PrimaryKey
    var id: String = "",
    var title: String = "",
    var manufacturerId: String = "",
    var manufacturer: String = "",
    var modelId: String = "",
    var model: String = "",
    var version: String = "",
    var versionId: String = "",
    @SerializedName("currentMiles")
    var currentMiles: Double = 0.0,
    @SerializedName("registrationDate")
    var yearOfRegistration: String = "",
    var color: String = "",
    var images: MutableList<String> = mutableListOf("https://drop.ndtv.com/albums/AUTO/2019-bmw-x7-suv/30.jpg"),
    @SerializedName("minPrice")
    var price: Double = 0.0,
    @Expose(serialize = false, deserialize = false)
    @Ignore
    var uris: MutableList<Uri> = mutableListOf(),
    var owner: String = ""
) : Parcelable

data class Bid(
    @SerializedName("id")
    var carId: String = "",
    var creator:User=User(),
    var price: Double = 0.0
)