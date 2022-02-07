package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.databinding.ItemListBinding
import com.android.anifind.presentation.adapter.SearchAdapter.ViewHolder

class SearchAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var searches: List<String> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun submitList(list: List<String>) {
        searches = list
    }
    var onItemClick: ((String) -> Unit)? = null

    override fun getItemCount(): Int = searches.size

    override fun onBindViewHolder(holder: ViewHolder, index: Int) = holder.bind(searches[index])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    inner class ViewHolder(
        private val binding: ItemListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(request: String) = with(binding) {
            item.text = request
            itemView.setOnClickListener { onItemClick?.invoke(request) }
        }
    }
}