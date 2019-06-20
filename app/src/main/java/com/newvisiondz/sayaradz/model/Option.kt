package com.newvisiondz.sayaradz.model

import com.google.gson.annotations.SerializedName

class Option(
    @SerializedName("name")
    var name: String,
    @SerializedName("values")
    var values: MutableList<Value>
)