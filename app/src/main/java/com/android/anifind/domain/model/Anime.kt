package com.android.anifind.domain.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Anime(
    val id: Int,
    val name: String,
    val russian: String,
    val image: Image,
    val kind: String,
    val score: Double,
    val status: String,
    val episodes: Int,
    @SerializedName("episodes_aired")
    val episodesAired: Int,
    @SerializedName("aired_on")
    val airedOn: String?,
) {
    val entity: AnimeEntity get() = AnimeEntity(
        id = id,
        name = russian.let { if (it.isNotEmpty()) it else name },
        original = name,
        imageUrl = "https://shikimori.one" + image.original,
        kind = kind,
        score = if (score == 0.0) "" else score.toString(),
        status = status,
        episodesInfo = when {
            episodes == 1 -> "1 эп"
            episodesAired == 0 -> "$episodesAired из ? эп"
            else -> "$episodesAired из $episodes эп"
        },
        year = airedOn?.substringBefore("-") ?: "",
        date = if (airedOn.isNullOrEmpty()) {
            "Дата выхода неизвестна"
        } else {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
            val formatter = SimpleDateFormat("dd MMMM y", Locale("ru"))
            formatter.format(parser.parse(airedOn)!!)
        }
    )
}