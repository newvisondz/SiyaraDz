package com.newvisiondz.sayara.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MutableListConverters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromString(value: String): MutableList<String> {
            val listType = object : TypeToken<ArrayList<String>>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromArrayList(list: MutableList<String>): String {
            return Gson().toJson(list)
        }
    }
}