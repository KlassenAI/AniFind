package com.android.anifind.domain.model

enum class WatchStatus(var title: String) {
    WATCHING("смотрю"),
    PLANNED("запланировано"),
    COMPLETED("просмотренно"),
    DROPPED("брошено"),
    REWATCHING("пересматриваю"),
    HOLD("отложено");

    companion object {
        fun getItem(index: Int) = values().first { it.ordinal == index }
        fun titles() = arrayOf("не смотрел(а)") + values().map { it.title }.toTypedArray()
    }
}