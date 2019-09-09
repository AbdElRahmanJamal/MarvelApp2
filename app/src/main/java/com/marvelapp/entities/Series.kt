package com.marvelapp.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Series(

        @SerializedName("available") val available: Int,
        @SerializedName("collectionURI") val collectionURI: String?,
        @SerializedName("items") val items: List<Items>?,
        @SerializedName("returned") val returned: Int
) : Parcelable