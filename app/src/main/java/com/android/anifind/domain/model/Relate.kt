package com.android.anifind.domain.model

import com.google.gson.annotations.SerializedName

data class Relate (
	val relation : String,
	@SerializedName("relation_russian")
	val relation_russian : String,
	val animeResponse : AnimeResponse
)