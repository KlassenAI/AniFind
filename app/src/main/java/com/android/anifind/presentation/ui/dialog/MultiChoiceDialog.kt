package com.android.anifind.presentation.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.android.anifind.Constants
import com.android.anifind.domain.model.MultiChoiceDialogType
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MultiChoiceDialog: DialogFragment() {

    private lateinit var listener: MultiChoiceDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as MultiChoiceDialogListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val type = arguments?.getSerializable(Constants.KEY_TYPE) as MultiChoiceDialogType
        val title = arguments?.getString(Constants.KEY_TITLE)!!
        val strings = arguments?.getStringArray(Constants.KEY_STRING_ARRAY)!!
        val booleans = arguments?.getBooleanArray(Constants.KEY_BOOLEAN_ARRAY)!!
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMultiChoiceItems(strings, booleans) { _, _, _ -> }
            .setPositiveButton("Ок") { _, _ -> listener.positiveAction(type, booleans) }
            .setNegativeButton("Отмена") { _, _ -> listener.negativeAction(type) }
            .setNeutralButton("Очистить") { _, _ -> listener.neutralAction(type) }
            .create()
    }

    interface MultiChoiceDialogListener {
        fun positiveAction(type: MultiChoiceDialogType, booleans: BooleanArray)
        fun negativeAction(type: MultiChoiceDialogType)
        fun neutralAction(type: MultiChoiceDialogType)
    }
}