package com.android.anifind.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.anifind.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

fun Fragment.showMultiChoiceDialog(
    title: String, data: Array<String>, booleans: BooleanArray,
    positiveAction: () -> Unit, negativeAction: () -> Unit, neutralAction: () -> Unit
) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMultiChoiceItems(data, booleans) { _, _, _ -> }
        .setPositiveButton("Ок") { _, _ -> positiveAction.invoke() }
        .setNegativeButton("Отмена") { _, _ -> negativeAction.invoke() }
        .setNeutralButton("Очистить") { _, _ -> neutralAction.invoke() }
        .show()
}
