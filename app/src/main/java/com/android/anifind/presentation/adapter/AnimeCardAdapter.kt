package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.anifind.databinding.CardAnimeBinding
import com.android.anifind.domain.model.AnimeEntity

class AnimeCardAdapter : NewGenericAdapter<AnimeEntity, CardAnimeBinding>() {

    private var onItemClick: ((item: AnimeEntity) -> Unit)? = null

    fun setOnItemClickListener(action: (item: AnimeEntity) -> Unit) {
        onItemClick = action
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): CardAnimeBinding {
        return CardAnimeBinding.inflate(inflater, parent, false)
    }

    override fun bind(
        binding: CardAnimeBinding, itemView: View, item: AnimeEntity, position: Int
    ) = with(binding) {
        poster.setImage(item.imageUrl)
        title.text = item.name
        title.invalidate()
        extra.hide()
        itemView.setOnClickListener { onItemClick?.invoke(item) }
    }
}