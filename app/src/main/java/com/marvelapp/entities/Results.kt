package com.marvelapp.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Results(

        @SerializedName("id") val id: Int,
        @SerializedName("title") val title : String?,
        @SerializedName("name") val name: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("modified") val modified: String?,
        @SerializedName("thumbnail") val thumbnail: Thumbnail?,
        @SerializedName("resourceURI") val resourceURI: String?,
        @SerializedName("comics") val comics: Comics?,
        @SerializedName("series") val series: Series?,
        @SerializedName("stories") val stories: Stories?,
        @SerializedName("events") val events: Events?,
        @SerializedName("urls") val urls: List<Urls>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Thumbnail::class.java.classLoader),
        parcel.readString(),
        parcel.readParcelable(Comics::class.java.classLoader),
        parcel.readParcelable(Series::class.java.classLoader),
        parcel.readParcelable(Stories::class.java.classLoader),
        parcel.readParcelable(Events::class.java.classLoader),
        parcel.createTypedArrayList(Urls)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(modified)
        parcel.writeParcelable(thumbnail, flags)
        parcel.writeString(resourceURI)
        parcel.writeParcelable(comics, flags)
        parcel.writeParcelable(series, flags)
        parcel.writeParcelable(stories, flags)
        parcel.writeParcelable(events, flags)
        parcel.writeTypedList(urls)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Results> {
        override fun createFromParcel(parcel: Parcel): Results {
            return Results(parcel)
        }

        override fun newArray(size: Int): Array<Results?> {
            return arrayOfNulls(size)
        }
    }

}