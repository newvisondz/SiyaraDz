package com.newvisiondz.sayaradz.model

import com.google.gson.annotations.SerializedName

class Color() {
    @SerializedName("id")
    var id=""
    @SerializedName("name")
    var name=""
    @SerializedName("value")
    var value=""
    constructor(id:String,name: String,value:String):this() {
        this.id=id
        this.name=name
        this.value=value
    }
}