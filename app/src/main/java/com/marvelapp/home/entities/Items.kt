package com.marvelapp.home.entities
import com.google.gson.annotations.SerializedName

data class Items (

	@SerializedName("resourceURI") val resourceURI : String,
	@SerializedName("name") val name : String
)