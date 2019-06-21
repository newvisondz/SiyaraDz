package com.newvisiondz.sayaradz.model

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