package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.databinding.CardAnimeBinding
import com.android.anifind.domain.model.AnimeEntity

class AnimeCardAdapter(
    private val listener: OnItemClickListener,
) : GenericAdapter<AnimeEntity, AnimeCardAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun notifyItemClicked(animeEntity: AnimeEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        CardAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(val binding: CardAnimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: AnimeEntity) = with(binding) {
            poster.setImage(entity.imageUrl)
            title.text = entity.name
            title.invalidate()
            extra.hide()
            itemView.setOnClickListener { listener.notifyItemClicked(entity) }
        }
    }
}