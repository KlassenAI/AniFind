package com.android.anifind.data.db

import androidx.room.TypeConverter
import com.android.anifind.domain.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GenreConverter {

    private val type = object : TypeToken<List<Genre>>() {}.type

    @TypeConverter
    fun convertTo(list: List<Genre>?): String? = list?.let { Gson().toJson(it, type) }

    @TypeConverter
    fun convertFrom(string: String?): List<Genre>? = string?.let { Gson().fromJson(it, type) }
}

class StudioConverter {

    private val type = object : TypeToken<List<Studio>>() {}.type

    @TypeConverter
    fun convertTo(list: List<Studio>?): String? = list?.let { Gson().toJson(it, type) }

    @TypeConverter
    fun convertFrom(string: String?): List<Studio>? = string?.let { Gson().fromJson(it, type) }
}

class ScreenshotConverter {

    private val type = object : TypeToken<List<Screenshot>>() {}.type

    @TypeConverter
    fun convertTo(list: List<Screenshot>?): String? = list?.let { Gson().toJson(it, type) }

    @TypeConverter
    fun convertFrom(string: String?): List<Screenshot>? = string?.let { Gson().fromJson(it, type) }
}

class WatchStatusConverter {

    @TypeConverter
    fun convertTo(obj: WatchStatus?): String? = obj?.name

    @TypeConverter
    fun convertFrom(string: String?): WatchStatus? = string?.let { enumValueOf<WatchStatus>(it) }
}
