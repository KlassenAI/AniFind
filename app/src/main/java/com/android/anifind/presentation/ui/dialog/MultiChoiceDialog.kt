package com.android.anifind.presentation.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.android.anifind.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MultiChoiceDialog: DialogFragment() {

    private lateinit var listener: Listener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as Listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val type = arguments?.getSerializable(Constants.KEY_TYPE) as Type
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

    enum class Type {
        GENRE,
        STUDIO
    }

    interface Listener {
        fun positiveAction(type: Type, booleans: BooleanArray)
        fun negativeAction(type: Type)
        fun neutralAction(type: Type)
    }
}