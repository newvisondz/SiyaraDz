package com.newvisiondz.sayara.model

import com.google.gson.annotations.SerializedName

class Model {
    @SerializedName("id")
    var id: String = ""
    @SerializedName("name")
    var name = ""
    @SerializedName("colors")
    var colors = mutableListOf<Color>()
    @SerializedName("options")
    var options = mutableListOf<Option>()
    @SerializedName("images")
    var images = mutableListOf<String>()

}