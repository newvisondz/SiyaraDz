package com.newvisiondz.sayaradz.model

class Brand() {
    var id = 0
    var name = ""
    var manufacturer = ""
    var imageId = 0
    constructor(id:Int,name:String,manufacturer:String,imageId:Int):this(){
        this.id = id
        this.name = name
        this.manufacturer = manufacturer
        this.imageId = imageId
    }


    class BrandSet(list: MutableList<Brand>) {
        private var brands = list
        private var output = list

        fun filterByName(vararg names: String): BrandSet {
            output = output.filter { brand ->
                names.any { name ->
                    brand.name!!.toLowerCase().contains(name.toLowerCase())
                }
            }.toMutableList()
            return this
        }

        fun filterByManufacturer(vararg manufacturers: String): BrandSet {
            output = output.filter { brand ->
                manufacturers.any { manufacturer ->
                    brand.manufacturer.toLowerCase().contains(manufacturer.toLowerCase())
                }
            }.toMutableList()
            return this
        }

        fun find(arg: String): BrandSet {
            output =
                output.filter { brand -> brand.name.contains(arg) || brand.manufacturer.contains(arg) }.toMutableList()
            return this
        }

        fun get(): MutableList<Brand> {
            return output
        }
    }
}