package com.newvisiondz.sayara.model

import android.graphics.Bitmap
import android.net.Uri

data class UsedCar(
    var id: Int = 0,
    var gearBoxType: String="",
    var currrentMiles: Double=0.0,
    var carBrand: String="",
    var price: Double=0.0,
    var yearOfRegistration: String="",
    var color: String="",
    var uris: MutableList<Uri> = mutableListOf(),
    var adresse :String="",
    var image :String="https://drop.ndtv.com/albums/AUTO/2019-bmw-x7-suv/30.jpg",
    var carModel: String=""
    ) {
    var bitmap:Bitmap?=null
}