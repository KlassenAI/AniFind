package com.android.anifind.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.R
import com.android.anifind.databinding.ItemAnimeBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.extensions.setImage

class AnimeAdapter : RecyclerView.Adapter<AnimeAdapter.ViewHolder>() {

    var animes: List<Anime> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = animes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(animes[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    inner class ViewHolder(val binding: ItemAnimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime) {
            binding.apply { poster.setImage(anime.image.original) }
            if (anime.isFavorite == true) {
                binding.btnFavorite.setImage(R.drawable.ic_bookmark_filled)
            }
            Log.d("anime", anime.toString())
        }
    }
}

private fun ImageButton.setImage(@DrawableRes id: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, id))
}
