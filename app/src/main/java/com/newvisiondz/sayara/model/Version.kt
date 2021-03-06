package com.newvisiondz.sayara.model

import com.google.gson.annotations.SerializedName

data class Version(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("colors")
    var colors: MutableList<Color>,
    @SerializedName("options")
    var options: MutableList<Option>
)
data class CarInfo(
    val id:String ="",
    @SerializedName("brand",alternate= ["name"])
    val name:String=""
)