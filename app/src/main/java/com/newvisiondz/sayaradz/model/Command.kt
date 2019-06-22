package com.newvisiondz.sayaradz.model

import com.google.gson.annotations.SerializedName

data class Command(
    @SerializedName("vehicles")
    val cars: MutableList<String>,
    @SerializedName("tarif")
    val price: Double
)