package com.android.anifind.presentation.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.android.anifind.R
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.domain.model.WatchStatus.*
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

fun View.hide() {
    isVisible = false
}

fun ImageView.setImage(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(CircularProgressDrawable(this.context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        })
        .centerCrop()
        .error(R.drawable.error_load)
        .fallback(R.drawable.error_load)
        .into(this)
}

fun ImageButton.setDraw(@DrawableRes id: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, id))
}

fun changeFavoriteButtonDraw(btn: ImageButton, favorite: Boolean) = when (favorite) {
    true -> btn.setDraw(R.drawable.ic_bookmark_filled)
    else -> btn.setDraw(R.drawable.ic_bookmark_border)
}

fun changeStatusButtonDraw(btn: ImageButton, status: WatchStatus) = when (status) {
    NO -> btn.setDraw(R.drawable.ic_add)
    WATCHING -> btn.setDraw(R.drawable.ic_play)
    PLANNED -> btn.setDraw(R.drawable.ic_calendar_item)
    COMPLETED -> btn.setDraw(R.drawable.ic_done)
    HOLD -> btn.setDraw(R.drawable.ic_pause)
    DROPPED -> btn.setDraw(R.drawable.ic_cancel)
}

fun View.snackbar(text: String, action: (() -> Unit)? = null) = Snackbar
    .make(this, text, Snackbar.LENGTH_SHORT)
    .setAction("Отмена") { action?.invoke() }
    .show()