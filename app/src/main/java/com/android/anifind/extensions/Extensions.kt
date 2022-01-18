package com.android.anifind.extensions

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.android.anifind.Constants
import com.android.anifind.R
import com.bumptech.glide.Glide

fun View.hide() { isVisible = false }
fun View.conceal() { isVisible = false }

fun ImageView.setImage(url: String) {
    Glide.with(this)
        .load(Constants.IMAGE_URL + url)
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