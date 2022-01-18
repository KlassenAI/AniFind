package com.android.anifind.extensions

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.completeView() = (editText as? AutoCompleteTextView)
fun TextInputLayout.setAdapter(adapter: ArrayAdapter<String>) = completeView()?.setAdapter(adapter)
fun TextInputLayout.setText(value: String) = completeView()?.setText(value, false)
fun TextInputLayout.clear() = setText(adapter()?.getItem(0).toString())
private fun TextInputLayout.adapter() = completeView()?.adapter