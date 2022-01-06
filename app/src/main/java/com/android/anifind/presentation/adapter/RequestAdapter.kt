package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.databinding.ItemListBinding
import com.android.anifind.presentation.adapter.RequestAdapter.ViewHolder

class RequestAdapter(
    private var requests: ArrayList<String> = arrayListOf()
) : RecyclerView.Adapter<ViewHolder>() {

    var onRequestClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request: String = requests[position]
        holder.bind(request)
    }

    override fun getItemCount(): Int = requests.size

    fun setData(list: ArrayList<String>) {
        requests = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(request: String) {
            binding.item.text = request
            itemView.setOnClickListener { onRequestClick?.invoke(request) }
        }
    }
}