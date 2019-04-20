package com.newvisiondz.sayaradz.model

import android.net.Uri

class Bid() {
    var id = 0
    var title = ""
    var owner = ""
    var city = ""
    var price = ""
    var options = listOf<String>()
    var imageId = 0
    var uris = mutableListOf<Uri>()
    constructor(id:Int,title:String,owner:String,city:String,price:String,imageId:Int):this(){
        this.id = id
        this.title = title
        this.owner = owner
        this.city = city
        this.price = price
        this.imageId = imageId
    }

}