package com.newvisiondz.sayaradz.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Brand() {
    @SerializedName("id")
    @Expose
    var id = 0
    @SerializedName("brand")
    @Expose
    var name = ""
    @SerializedName("logo")
    @Expose
    var logo = ""

  //  var manufacturer = ""

    constructor(id: Int, name: String, imageId: String):this(){
        this.id = id
        this.name = name
        //this.manufacturer = manufacturer
        this.logo = imageId
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

//        fun filterByManufacturer(vararg manufacturers: String): BrandSet {
//            output = output.filter { brand ->
//                manufacturers.any { manufacturer ->
//                    brand.manufacturer.toLowerCase().contains(manufacturer.toLowerCase())
//                }
//            }.toMutableList()
//            return this
//        }

        fun find(arg: String): BrandSet {
            output =
                output.filter { brand -> brand.name.contains(arg)  }.toMutableList()
            return this
        }

        fun get(): MutableList<Brand> {
            return output
        }
    }
}