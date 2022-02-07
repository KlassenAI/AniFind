package com.android.anifind.extensions

import android.view.View
import android.widget.ImageButton
import androidx.core.view.isVisible
import com.android.anifind.R
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.presentation.adapter.setDraw

fun View.conceal() { isVisible = false }

fun changeFavoriteButtonDraw(btn: ImageButton, favorite: Boolean) = when (favorite) {
    true -> btn.setDraw(R.drawable.ic_bookmark_filled)
    else -> btn.setDraw(R.drawable.ic_bookmark_border)
}

fun changeStatusButtonDraw(btn: ImageButton, status: WatchStatus) = when (status) {
    WatchStatus.NO -> btn.setDraw(R.drawable.ic_edit)
    WatchStatus.WATCHING -> btn.setDraw(R.drawable.ic_play)
    WatchStatus.PLANNED -> btn.setDraw(R.drawable.ic_time)
    WatchStatus.COMPLETED -> btn.setDraw(R.drawable.ic_done)
    WatchStatus.HOLD -> btn.setDraw(R.drawable.ic_pause)
    WatchStatus.DROPPED -> btn.setDraw(R.drawable.ic_clear)
}