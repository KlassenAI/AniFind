package com.android.anifind.domain.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "animes")
data class Anime(
    @PrimaryKey val id: Int,
    val name: String,
    val russian: String,
    @Embedded val image: Image,
    val url: String,
    val kind: String,
    val score: Double,
    val status: String,
    val episodes: Int,
    @SerializedName("episodes_aired")
    @ColumnInfo(name = "episodes_aired")
    val episodesAired: Int,
    @SerializedName("aired_on")
    @ColumnInfo(name = "aired_on")
    val airedOn: String?,
    @SerializedName("released_on")
    @ColumnInfo(name = "released_on")
    val releasedOn: String?,

    val rating: String?,
    val duration: Int?,
    val description: String?,
    val franchise: String?,
    @SerializedName("next_episode_at")
    val nextEpisodeAt: String?,
    val genres: List<Genre>?,
    val studios: List<Studio>?,
    val screenshots: List<Screenshot>?,

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean?,
    @ColumnInfo(name = "watch_status")
    var watchStatus: WatchStatus?,
    @ColumnInfo(name = "add_date")
    var addDate: String?,
    @ColumnInfo(name = "update_date")
    var updateDate: String?
)