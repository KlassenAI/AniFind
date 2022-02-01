package com.android.anifind.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.anifind.domain.model.IDiffUtilItem

class GenericDiffUtilCallback<T : IDiffUtilItem>(
    private val oldList: List<T>,
    private val newList: List<T>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(old: Int, new: Int) = oldList[old].key() == newList[new].key()
    override fun areContentsTheSame(old: Int, new: Int) = oldList[old] == newList[new]
}