package com.android.anifind.presentation.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.android.anifind.Constants
import com.android.anifind.R
import com.android.anifind.databinding.ItemAnimeBinding
import com.android.anifind.domain.model.Anime
import com.bumptech.glide.Glide

class AnimePagingAdapter : PagingDataAdapter<Anime, AnimePagingAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val anime = getItem(position)
        holder.bind(anime)
    }

    class ViewHolder(private val binding: ItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime?) {
            if (anime != null) {
                binding.apply {
                    nameOriginal.text = anime.name
                    nameRussian.text = anime.russian
                    if (anime.airedOn == null) {
                        date.isVisible = false
                    } else {
                        date.text = anime.airedOn.substringBefore("-")
                    }
                    if (anime.score == 0.0) {
                        rate.isVisible = false
                    } else {
                        rate.text = anime.score.toString()
                    }

                    Glide.with(this.anime)
                        .load(getImageUrl(anime.image.original))
                        .placeholder(CircularProgressDrawable(itemView.context).apply {
                            strokeWidth = 5f
                            centerRadius = 30f
                            start()
                        })
                        .centerCrop()
                        .error(R.drawable.error_load)
                        .fallback(R.drawable.error_load)
                        .into(poster)
                }
            }
        }

        private fun getImageUrl(url: String) = Constants.IMAGE_URL + url
    }

    object DiffCallback : DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(old: Anime, new: Anime): Boolean = old.id == new.id
        override fun areContentsTheSame(old: Anime, new: Anime): Boolean = old == new
    }
}