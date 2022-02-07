package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.android.anifind.databinding.ItemGenreBinding
import com.android.anifind.domain.model.Genre
import java.util.*

class GenreAdapter: NewGenericAdapter<Genre, ItemGenreBinding>() {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemGenreBinding {
        return ItemGenreBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ItemGenreBinding, itemView: View, item: Genre, position: Int) {
        with(binding) {
            genre.text = item.russian.toLowerCase(Locale.ROOT)
            if (isLastItem(position)) comma.isVisible = false
        }
    }
}