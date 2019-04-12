package com.newvisiondz.sayaradz.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Value() {
    @SerializedName("id")
    var id: String=""
    @SerializedName("value")
    var value: String=""

    constructor(id: String, value: String) : this() {
        this.value = value
        this.id = id
    }
}