package com.android.anifind.domain.model

import com.google.gson.annotations.SerializedName

data class Anime(
    // Быстрый поиск
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
    @SerializedName("aired_on") val airedOn: String?,
    @SerializedName("released_on") val releasedOn: String,
    // Расширенный поиск
    val rating: String,
    val english: List<String>,
    val japanese: List<String>,
    val synonyms: List<String>,
    @SerializedName("license_name_ru") val licenseNameRu: String,
    val duration: Int,
    val description: String,
    @SerializedName("description_html") val descriptionHtml: String,
    @SerializedName("description_source") val descriptionSource: String,
    val franchise: String,
    val favoured: Boolean,
    val anons: Boolean,
    val ongoing: Boolean,
    @SerializedName("thread_id") val threadId: Int,
    @SerializedName("topic_id") val topicId: Int,
    @SerializedName("myanimelist_id") val myAnimeListId: Int,
    @SerializedName("rates_scores_stats") val ratesScoresStats: List<RatesScoresStat>,
    @SerializedName("rates_statuses_stats") val ratesStatusesStats: List<RatesStatusesStat>,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("next_episode_at") val nextEpisodeAt: String,
    @SerializedName("fansubbers") val fanSubbers: List<String>,
    @SerializedName("fandubbers") val fanDubbers: List<String>,
    @SerializedName("licensors") val licensors: List<String>,
    val genres: List<Genre>,
    val studios: List<Studio>,
    val videos: List<Video>,
    val screenshots: List<Screenshot>,
    @SerializedName("user_rate") val userRate: String
)