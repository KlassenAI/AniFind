package com.android.anifind.extensions

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filterable
import android.widget.ListAdapter
import com.google.android.material.textfield.TextInputLayout

fun <T> TextInputLayout.setAdapter(adapter: T) where T : ListAdapter?, T : Filterable? {
    (editText as? AutoCompleteTextView)?.setAdapter(adapter)
}

fun TextInputLayout.setText(value: String) {
    (editText as? AutoCompleteTextView)?.setText(value, false)
}

fun <T> TextInputLayout.clear(adapter: T) where T : ListAdapter?, T : Filterable? {
    setText(adapter?.getItem(0).toString())
}

fun TextInputLayout.getParam(
    adapter: ArrayAdapter<String>, items: List<String?>
): String? {
    return items[adapter.getPosition((editText as? AutoCompleteTextView)?.text.toString())]
}
