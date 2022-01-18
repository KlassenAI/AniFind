package com.android.anifind.data.db

import android.content.Context
import androidx.room.*
import com.android.anifind.domain.model.Anime
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(
    entities = [Anime::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    GenreConverter::class,
    StudioConverter::class,
    ScreenshotConverter::class,
    WatchStatusConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao

    companion object {
        fun getDb(@ApplicationContext context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "animes").build()
    }
}