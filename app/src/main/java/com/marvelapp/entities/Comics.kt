package com.marvelapp.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Comics(

        @SerializedName("available") val available: Int,
        @SerializedName("collectionURI") val collectionURI: String?,
        @SerializedName("items") val items: List<Items>?,
        @SerializedName("returned") val returned: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.createTypedArrayList(Items),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(available)
        parcel.writeString(collectionURI)
        parcel.writeTypedList(items)
        parcel.writeInt(returned)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Comics> {
        override fun createFromParcel(parcel: Parcel): Comics {
            return Comics(parcel)
        }

        override fun newArray(size: Int): Array<Comics?> {
            return arrayOfNulls(size)
        }
    }
}