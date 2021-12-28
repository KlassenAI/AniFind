package com.android.anifind.domain.model

import com.google.gson.annotations.SerializedName

data class Studio (
	val id : Int,
	val name : String,
	@SerializedName("filtered_name") val filteredName : String,
	val real : Boolean,
	val image : String
)
