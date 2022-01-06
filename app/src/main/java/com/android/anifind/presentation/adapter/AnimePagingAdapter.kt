package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
        getItem(position)?.let { holder.bind(it) }
    }

    class ViewHolder(private val binding: ItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime) {
            binding.apply {
                nameOriginal.text = anime.name
                nameRussian.setOrHideText(anime.russian)
                date.setOrHideText(anime.airedOn?.getYear())
                rate.setOrHideNumber(anime.score)
                poster.setImage(anime.image.original)
            }
        }

        private fun getImageUrl(url: String) = Constants.IMAGE_URL + url

        private fun TextView.setOrHideText(text: String?) {
            if (text.isNullOrEmpty()) {
                isVisible = false
            } else {
                this.text = text
            }
        }

        private fun String.getYear() = this.substringBefore("-")

        private fun TextView.setOrHideNumber(number: Double) {
            if (number == 0.0) {
                isVisible = false
            } else {
                text = number.toString()
            }
        }

        private fun ImageView.setImage(url: String) {
            Glide.with(this)
                .load(getImageUrl(url))
                .placeholder(CircularProgressDrawable(itemView.context).apply {
                    strokeWidth = 5f
                    centerRadius = 30f
                    start()
                })
                .centerCrop()
                .error(R.drawable.error_load)
                .fallback(R.drawable.error_load)
                .into(this)
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(old: Anime, new: Anime): Boolean = old.id == new.id
        override fun areContentsTheSame(old: Anime, new: Anime): Boolean = old == new
    }
}