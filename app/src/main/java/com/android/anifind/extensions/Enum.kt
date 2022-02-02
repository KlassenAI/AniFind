package com.android.anifind.extensions

import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.domain.model.WatchStatus.*

fun getStatusChangeMessage(old: WatchStatus, new: WatchStatus): String = when(new) {
    NO -> "Удалено из ${
        when (old) {
            WATCHING -> "просматриваемого"
            PLANNED -> "запланированного"
            COMPLETED -> "просмотренного"
            HOLD -> "отложенного"
            DROPPED -> "брошенного"
            NO -> ""
        }
    }"
    WATCHING -> "Добавлено в просматриваемое"
    PLANNED -> "Добавлено в запланированное"
    COMPLETED -> "Добавлено в просмотренное"
    HOLD -> "Добавлено в отложенное"
    DROPPED -> "Добавлено в брошенное"
}

fun getFavoriteChangeMessage(favorite: Boolean) = when (favorite) {
    true -> "Добавлено в избранное"
    else -> "Удалено из избранного"
}