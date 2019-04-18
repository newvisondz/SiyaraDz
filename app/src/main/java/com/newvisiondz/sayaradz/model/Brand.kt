package com.newvisiondz.sayaradz.model

import android.graphics.Bitmap
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Brand() {
    @SerializedName("id")
    var id = ""
    @SerializedName("brand")
    var name = ""
    @SerializedName("logo")
    var logo = ""
    var image: Bitmap? = null

  //  var manufacturer = ""

    constructor(id: String, name: String, logo: String):this(){
        this.id = id
        this.name = name
        //this.manufacturer = manufacturer
        this.logo = logo
    }

    class BrandSet(list: MutableList<Brand>) {
        private var brands = list
        private var output = list

        fun filterByName(vararg names: String): BrandSet {
            output = output.filter { brand ->
                names.any { name ->
                    brand.name.toLowerCase().contains(name.toLowerCase())
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