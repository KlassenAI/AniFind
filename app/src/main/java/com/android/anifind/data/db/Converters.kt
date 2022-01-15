package com.android.anifind.data.db

import androidx.room.TypeConverter
import com.android.anifind.domain.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

abstract class ListConverter<T> {

    private val gson = Gson()
    private val type = object : TypeToken<List<T>>() {}.type

    @TypeConverter
    fun convert(obj: List<T>?): String? = obj?.let { gson.toJson(it, type) }

    @TypeConverter
    fun unpack(obj: String?): List<T>? = obj?.let { gson.fromJson(obj, type) }
}

class GenreConverter : ListConverter<Genre>()
class StudioConverter : ListConverter<Studio>()
class ScreenshotConverter : ListConverter<Screenshot>()

class WatchStatusConverter {

    @TypeConverter
    fun convert(obj: WatchStatus?): String? = obj?.name

    @TypeConverter
    fun unpack(obj: String?): WatchStatus? = obj?.let { enumValueOf<WatchStatus>(it) }
}
