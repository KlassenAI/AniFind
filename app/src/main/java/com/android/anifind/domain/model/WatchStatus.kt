package com.android.anifind.domain.model

enum class WatchStatus(var title: String) {
    NO("не смотрел(а)"),
    WATCHING("смотрю"),
    PLANNED("запланировано"),
    COMPLETED("просмотренно"),
    HOLD("отложено"),
    DROPPED("брошено");

    companion object {
        fun getItem(index: Int) = values().first { it.ordinal == index }
        fun titles() = values().map { it.title }.toTypedArray()
    }
}