package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.anifind.databinding.ItemAnimeBinding
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.extensions.changeFavoriteButtonDraw
import com.android.anifind.extensions.changeStatusButtonDraw

class AnimeAdapter(
    private val listener: Listener
) : NewGenericAdapter<AnimeEntity, ItemAnimeBinding>() {

    interface Listener {
        fun onItemClick(item: AnimeEntity)
        fun onFavoriteButtonClick(item: AnimeEntity, position: Int)
        fun onStatusButtonClick(item: AnimeEntity, position: Int)
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemAnimeBinding {
        return ItemAnimeBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ItemAnimeBinding, itemView: View, item: AnimeEntity, position: Int) {
        with(binding) {
            changeFavoriteButtonDraw(btnFavorite, item.isFavorite)
            changeStatusButtonDraw(btnStatus, item.watchStatus)
            poster.setImage(item.imageUrl)
            name.text = item.name
            date.text = item.year
            episodes.hide()
            score.text = item.score
            itemView.setOnClickListener { listener.onItemClick(item) }
            btnFavorite.setOnClickListener { listener.onFavoriteButtonClick(item, position) }
            btnStatus.setOnClickListener { listener.onStatusButtonClick(item, position) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads)
        else bind(holder.binding, getItem(position), payloads)
    }

    private fun bind(binding: ItemAnimeBinding, item: AnimeEntity, payloads: MutableList<Any>) {
        payloads.forEach {
            when (it) {
                is Boolean -> changeFavoriteButtonDraw(binding.btnFavorite, item.isFavorite)
                is WatchStatus -> changeStatusButtonDraw(binding.btnStatus, item.watchStatus)
            }
        }
    }
}