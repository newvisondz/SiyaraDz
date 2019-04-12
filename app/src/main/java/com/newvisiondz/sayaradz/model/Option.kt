package com.newvisiondz.sayaradz.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Option() {
    @SerializedName("name")
    var name: String=""
    @SerializedName("values")
    var values= mutableListOf<Value>()

    constructor(name: String, values: MutableList<Value>) : this() {
        this.name = name
        this.values = values
    }
}