package com.android.anifind.domain.model

enum class WatchStatus(var title: String) {
    WATCHING("смотрю"),
    PLANNED("запланировано"),
    COMPLETED("просмотренно"),
    HOLD("отложено"),
    DROPPED("брошено");

    companion object {
        fun getItem(index: Int): WatchStatus? {
            return if (index == -1) null else values().first { it.ordinal == index }
        }
        fun titles() = arrayOf("не смотрел(а)") + values().map { it.title }.toTypedArray()
    }
}
