package com.android.anifind.domain.model

import com.google.gson.annotations.SerializedName

data class Anime(
    val id: Int,
    val name: String,
    val russian: String,
    val image: Image,
    val url: String,
    val kind: String,
    val score: Double,
    val status: String,
    val episodes: Int,
    @SerializedName("episodes_aired") val episodesAired: Int,
    @SerializedName("aired_on") val airedOn: String,
    @SerializedName("released_on") val releasedOn: String
)