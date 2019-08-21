package com.newvisiondz.sayara.database

data class DatabaseUsedCar constructor(
    var id: Int = 0,
    var currrentMiles: Double = 0.0,
    var carBrand: String = "",
    var price: Double = 0.0,
    var yearOfRegistration: String = "",
    var color: String = "",
    var adresse: String = "",
    var image: String = "https://drop.ndtv.com/albums/AUTO/2019-bmw-x7-suv/30.jpg",
    var carModel: String = "",
    var version: String = ""
)

