package com.newvisiondz.sayaradz.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Model() {
    @SerializedName("id")
    var id:String = ""
    @SerializedName("name")
    var name = ""
    @SerializedName("colors")
    var colors= mutableListOf<Color>()
    @SerializedName("options")
    var options = mutableListOf<Option>()

    constructor(
        id: String,
        name: String,
        color: MutableList<Color>,
        options: MutableList<Option>
    ) : this() {
        this.id = id
        this.name = name
        this.colors = color
        this.options = options
    }
}