package com.newvisiondz.sayara.model

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "usedCar")
data class UsedCar(
    @PrimaryKey
    var id: Int = 0,
    var gearBoxType: String="",
    @SerializedName("currrentMiles")
    var currrentMiles: Double=0.0,
    var carBrand: String="",
    @SerializedName("minPrice       ")
    var price: Double=0.0,
    @SerializedName("registrationDate")
    var yearOfRegistration: String="",
    var color: String="",
    @Ignore
    var uris: MutableList<Uri> = mutableListOf(),
    var adresse :String="",
    var image :String="https://drop.ndtv.com/albums/AUTO/2019-bmw-x7-suv/30.jpg",
    var carModel: String="",
    var version: String=""

    ) {
    @Ignore
    var bitmap:Bitmap?=null
}