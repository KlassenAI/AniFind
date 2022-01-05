package com.android.anifind.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.anifind.R
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.navigateUp() {
    findNavController().navigateUp()
}

fun Fragment.navigateToSearchFragment() {
    findNavController().navigate(R.id.searchFragment)
}

fun Fragment.navigateToFilterFragment() {
    findNavController().navigate(R.id.filterFragment)
}

fun getYearList(): List<String> {
    val nextYear = SimpleDateFormat("yyyy", Locale.ROOT).format(Date()).toInt() + 1
    val arrayList = arrayListOf<String>()
    for (i in nextYear downTo 1970) arrayList.add(i.toString())
    return arrayList.toList()
}
