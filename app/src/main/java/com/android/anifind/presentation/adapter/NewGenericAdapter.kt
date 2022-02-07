package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.android.anifind.domain.model.IDiffUtilItem

abstract class NewGenericAdapter<T : IDiffUtilItem, VB : ViewBinding> :
    RecyclerView.Adapter<NewGenericAdapter<T, VB>.ViewHolder>() {

    private var items: List<T> = emptyList()

    fun submitList(value: List<T>) {
        val diffResult = DiffUtil.calculateDiff(GenericDiffUtilCallback(items, value))
        items = value
        diffResult.dispatchUpdatesTo(this)
    }

    fun isLastItem(position: Int) = position == items.size.minus(1)

    fun getItem(position: Int) = items[position]

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        createBinding(LayoutInflater.from(parent.context), parent)
    )

    abstract fun createBinding(inflater: LayoutInflater, parent: ViewGroup): VB

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    abstract fun bind(binding: VB, itemView: View, item: T, position: Int)

    inner class ViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: T, position: Int) {
            bind(binding, itemView, item, position)
        }
    }
}