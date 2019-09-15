package com.newvisiondz.sayara.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Model(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("colors")
    var colors: MutableList<Color> = mutableListOf(),
    @SerializedName("options")
    var options: MutableList<Option> = mutableListOf(),
    @SerializedName("images")
    var images: MutableList<String> = mutableListOf()
) : Serializable