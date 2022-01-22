package com.android.anifind.extensions

import android.view.View
import androidx.core.view.isVisible

fun View.conceal() { isVisible = false }