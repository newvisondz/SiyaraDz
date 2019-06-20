package com.newvisiondz.sayaradz.model

import com.google.gson.annotations.SerializedName

class Color(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("value")
    var value: String
)