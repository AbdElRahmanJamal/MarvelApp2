package com.marvelapp.entities
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Items (

	@SerializedName("resourceURI") val resourceURI : String?,
	@SerializedName("name") val name : String?
): Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readString()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(resourceURI)
		parcel.writeString(name)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Items> {
		override fun createFromParcel(parcel: Parcel): Items {
			return Items(parcel)
		}

		override fun newArray(size: Int): Array<Items?> {
			return arrayOfNulls(size)
		}
	}
}