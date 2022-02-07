package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.databinding.ItemAnimeBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.domain.model.WatchStatus.*
import com.android.anifind.extensions.changeFavoriteButtonDraw
import com.android.anifind.extensions.changeStatusButtonDraw
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AnimePagingAdapter(
    private val listener: Listener
) : PagingDataAdapter<Anime, AnimePagingAdapter.ViewHolder>(DiffCallback) {

    interface Listener {
        fun onItemClick(item: Anime)
        fun onFavoriteButtonClick(item: Anime, position: Int)
        fun onStatusButtonClick(item: Anime, position: Int)
        fun onLoadAnimeFromDB(item: Anime, position: Int)
    }

    object DiffCallback : DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(old: Anime, new: Anime) = old.entity.id == new.entity.id
        override fun areContentsTheSame(old: Anime, new: Anime) = old == new
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val anime = getItem(position) ?: return
        holder.bind(anime, position)
        listener.onLoadAnimeFromDB(anime, position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }
        payloads.forEach { payload ->
            val item = getItem(position)
            if (item != null) {
                when (payload) {
                    is Boolean -> changeFavoriteButtonDraw(holder.binding.btnFavorite, item.entity.isFavorite)
                    is WatchStatus -> changeStatusButtonDraw(holder.binding.btnStatus, item.entity.watchStatus)
                }
            }
        }
    }

    inner class ViewHolder(val binding: ItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime, position: Int) = with(binding) {
            changeFavoriteButtonDraw(btnFavorite, anime.entity.isFavorite)
            changeStatusButtonDraw(btnStatus, anime.entity.watchStatus)
            poster.setImage(anime.entity.imageUrl)
            name.text = anime.entity.name
            bindByType(date = anime.entity.year, score = anime.entity.score)
            itemView.setOnClickListener { listener.onItemClick(anime) }
            btnFavorite.setOnClickListener { listener.onFavoriteButtonClick(anime, position) }
            btnStatus.setOnClickListener { listener.onStatusButtonClick(anime, position) }
        }

        private fun bindByType(
            date: String = "", eps: String = "", score: String?
        ) = with(binding) {
            if (date.isEmpty()) this.date.hide() else this.date.text = date
            if (eps.isEmpty()) this.episodes.hide() else this.episodes.text = eps
            if (score.isNullOrEmpty()) this.score.hide() else this.score.text = score
        }
    }
}