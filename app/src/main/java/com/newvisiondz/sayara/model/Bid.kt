package com.newvisiondz.sayara.model

import android.net.Uri

class Bid() {
    var id = 0
    var chassisNumber = 0
    var adresse = ""
    var price = 0.0
    private var currrentMiles =0.0
    var options = listOf<String>()
    var uris = mutableListOf<Uri>()

    constructor(id:Int,model:String,chassisNumber:Int,adresse:String,price:Double,currentMiles:Double):this(){
        this.id = id
        this.chassisNumber = chassisNumber
        this.adresse = adresse
        this.price = price
        this.currrentMiles=currentMiles
    }

}