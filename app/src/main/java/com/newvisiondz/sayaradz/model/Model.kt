package com.newvisiondz.sayaradz.model

class Model() {
    var id = 0
    var name = ""
    var attribute = ""
    var price = ""
    var imageId = 0
    constructor(id:Int,name:String,attribute:String,price:String,imageId:Int):this(){
        this.id = id
        this.name = name
        this.attribute = attribute
        this.price = price
        this.imageId = imageId
    }
}