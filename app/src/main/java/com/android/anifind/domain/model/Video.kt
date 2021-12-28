package com.android.anifind.domain.model

import com.google.gson.annotations.SerializedName

data class Video (
	val id : Int,
	val url : String,
	@SerializedName("image_url") val imageUrl : String,
	@SerializedName("player_url") val playerUrl : String,
	val name : String,
	val kind : String,
	val hosting : String
)
