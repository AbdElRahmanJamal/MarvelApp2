package com.marvelapp.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comics(

    @SerializedName("available") val available: Int,
    @SerializedName("collectionURI") val collectionURI: String?,
    @SerializedName("items") val items: List<Items>?,
    @SerializedName("returned") val returned: Int
) : Parcelable