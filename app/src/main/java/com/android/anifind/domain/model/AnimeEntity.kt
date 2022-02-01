package com.android.anifind.domain.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animes")
data class AnimeEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val original: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    val kind: String,
    val score: String,
    val status: String,
    @ColumnInfo(name = "episodes_info")
    val episodesInfo: String,
    val year: String,
    val date: String,

    @Embedded
    var info: AnimeInfo? = null,

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false,
    @ColumnInfo(name = "watch_status")
    var watchStatus: WatchStatus = WatchStatus.NO,
    @ColumnInfo(name = "add_date")
    var addDate: String = "",
    @ColumnInfo(name = "update_date")
    var updateDate: String = ""
): IDiffUtilItem {
    override fun key(): Int = id
}