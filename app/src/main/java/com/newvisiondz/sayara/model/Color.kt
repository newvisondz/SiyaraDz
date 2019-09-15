package com.newvisiondz.sayara.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Color(
    @SerializedName("id")
    var id: String="",
    @SerializedName("name")
    var name: String,
    @SerializedName("value")
    var value: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Color> {
        override fun createFromParcel(parcel: Parcel): Color {
            return Color(parcel)
        }

        override fun newArray(size: Int): Array<Color?> {
            return arrayOfNulls(size)
        }
    }
}