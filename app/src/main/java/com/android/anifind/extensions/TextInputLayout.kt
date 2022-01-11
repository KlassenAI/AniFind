package com.android.anifind.extensions

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filterable
import android.widget.ListAdapter
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.completeView() = (editText as? AutoCompleteTextView)
fun TextInputLayout.adapter() = completeView()?.adapter

fun <T> TextInputLayout.setAdapter(adapter: T) where T : ListAdapter?, T : Filterable? {
    completeView()?.setAdapter(adapter)
}

fun TextInputLayout.setText(value: String) {
    completeView()?.setText(value, false)
}

fun TextInputLayout.clear() {
    setText(adapter()?.getItem(0).toString())
}

fun TextInputLayout.getParam(
    adapter: ArrayAdapter<String>, items: List<String?>
): String? {
    return items[adapter.getPosition(completeView()?.text.toString())]
}
