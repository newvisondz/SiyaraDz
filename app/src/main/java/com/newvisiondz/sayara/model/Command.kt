package com.newvisiondz.sayara.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Command(
    val id:String="",
    @SerializedName("vehicles")
    val cars: MutableList<String>,
    @SerializedName("tarif")
    val price: Double
)

data class CommandConfirmed(
    val id:String="",
    val vehicle:String="",
    val automobiliste:String="",
    val accepted:Boolean=false,
    val payed:Boolean=false,
    @SerializedName("amount")
    val price: Double,
    val createdAt:Date
)