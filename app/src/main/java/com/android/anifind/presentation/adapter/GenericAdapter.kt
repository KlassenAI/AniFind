package com.android.anifind.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.domain.model.IDiffUtilItem

abstract class GenericAdapter<T : IDiffUtilItem, VH : RecyclerView.ViewHolder?>: RecyclerView.Adapter<VH>() {

    private var items: List<T> = emptyList()

    fun submitList(value: List<T>) {
        val diffCallback = GenericDiffUtilCallback(items, value)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = value
        diffResult.dispatchUpdatesTo(this)
    }

    fun getItem(position: Int) = items[position]

    override fun getItemCount(): Int = items.size
}