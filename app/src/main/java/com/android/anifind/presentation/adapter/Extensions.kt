package com.android.anifind.presentation.adapter

import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import java.text.SimpleDateFormat
import java.util.*

fun ImageButton.setDraw(@DrawableRes id: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, id))
}

fun TextView.setText(primary: String, secondary: String) {
    this.text = if (primary.isEmpty()) secondary else primary
}

fun TextView.setYear(date: String?) {
    if (date.isNullOrEmpty()) isVisible = false else text = date
}

fun String.getYear() = this.substringBefore("-")

fun TextView.setDate(date: String?) {
    text = if (date.isNullOrEmpty()) {
        "Дата выхода неизвестна"
    } else {
        val networkFormatter = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
        val appFormatter = SimpleDateFormat("dd MMMM y", Locale("ru"))
        appFormatter.format(networkFormatter.parse(date)!!)
    }
}

fun TextView.setEpisodes(aired: Int, total: Int) {
    text = String.format("%s из %s эпизодов", aired.toString(), total.toFormatString())
}

fun Int.toFormatString(): String = if (this == 0) "?" else this.toString()

fun TextView.setDouble(number: Double) {
    if (number == 0.0) isVisible = false else text = number.toString()
}
