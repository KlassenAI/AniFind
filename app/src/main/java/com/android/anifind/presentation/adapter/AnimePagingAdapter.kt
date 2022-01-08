package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.databinding.ItemAnimeBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.extensions.hide
import com.android.anifind.extensions.setImage
import com.android.anifind.presentation.adapter.AdapterType.*
import java.text.SimpleDateFormat
import java.util.*

class AnimePagingAdapter(
    private val type: AdapterType
) : PagingDataAdapter<Anime, AnimePagingAdapter.ViewHolder>(DiffCallback) {

    var onItemClick: ((Anime) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: ItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime) {
            itemView.setOnClickListener { onItemClick?.invoke(anime) }
            binding.apply {
                poster.setImage(anime.image.original)
                name.setNotEmptyText(anime.russian, anime.name)
                when(type) {
                    DEFAULT -> {
                        date.setOrHideText(anime.airedOn?.getYear())
                        episodes.hide()
                        rate.setOrHideNumber(anime.score)
                    }
                    ANONS -> {
                        date.setOrHideDate(anime.airedOn)
                        episodes.hide()
                        rate.hide()
                    }
                    ONGOING -> {
                        date.hide()
                        episodes.text = String.format("%s из %s эпизодов",
                            anime.episodesAired.toString(), anime.episodes.toFormatString())
                        rate.setOrHideNumber(anime.score)
                    }
                }
            }
        }

        private fun TextView.setNotEmptyText(maybeEmptyText: String, notEmptyText: String) {
            this.text = if (maybeEmptyText.isEmpty()) notEmptyText else maybeEmptyText
        }

        private fun TextView.setOrHideDate(date: String?) {
            text = formatDate(date) ?: "Дата выхода неизвестна"
        }

        private fun formatDate(date: String?): String? {
            if (date.isNullOrEmpty()) return null
            val networkFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
            val appFormatter = SimpleDateFormat("dd MMMM y", Locale.ROOT)
            return appFormatter.format(networkFormatter.parse(date)!!)
        }

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

        private fun Int.toFormatString(): String = if (this == 0) "?" else this.toString()
    }


    object DiffCallback : DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(old: Anime, new: Anime): Boolean = old.id == new.id
        override fun areContentsTheSame(old: Anime, new: Anime): Boolean = old == new
    }
}
