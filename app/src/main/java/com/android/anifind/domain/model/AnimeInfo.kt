package com.android.anifind.domain.model

import com.google.gson.annotations.SerializedName

data class AnimeInfo(
    val rating: String,
    val duration: Int,
    val description: String?,
    @SerializedName("description_html")
    val descriptionHtml: String?,
    val franchise: String?,
    @SerializedName("next_episode_at")
    val nextEpisodeAt: String?,
    val genres: List<Genre>,
    val studios: List<Studio>,
    val screenshots: List<Screenshot>,
)