package com.android.anifind.data.db

import android.content.Context
import androidx.room.*
import com.android.anifind.domain.model.AnimeEntity
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(
    entities = [AnimeEntity::class],
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
    abstract fun dao(): AnimeDao
    
    companion object {
        fun getFromContext(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
            context, AppDatabase::class.java, "animes"
        ).build()
    }
}