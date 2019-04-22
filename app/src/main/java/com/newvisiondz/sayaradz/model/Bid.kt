package com.newvisiondz.sayaradz.model

import android.net.Uri

class Bid() {
    var id = 0
    var owner = ""
    var model = ""
    var adresse = ""
    var price = 0.0
    private var currrentMiles =0.0
    var options = listOf<String>()
    var uris = mutableListOf<Uri>()

    constructor(id:Int,model:String,owner:String,adresse:String,price:Double,currentMiles:Double):this(){
        this.id = id
        this.owner = owner
        this.model = model
        this.adresse = adresse
        this.price = price
        this.currrentMiles=currentMiles
    }

}