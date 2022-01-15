package com.android.anifind.presentation.adapter

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.R
import com.android.anifind.databinding.ItemAnimeBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.domain.model.WatchStatus.*
import com.android.anifind.extensions.hide
import com.android.anifind.extensions.setImage
import com.android.anifind.presentation.adapter.AdapterType.*
import com.android.anifind.presentation.viewmodel.BaseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*

class AnimePagingAdapter(
    private val type: AdapterType,
    private val viewModel: BaseViewModel,
    private val lifecycle: LifecycleOwner
) : PagingDataAdapter<Anime, AnimePagingAdapter.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(old: Anime, new: Anime): Boolean = old.id == new.id
        override fun areContentsTheSame(old: Anime, new: Anime): Boolean = old == new
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            viewModel.getAnime(item.id).observe(lifecycle) { animeDb ->
                if (animeDb != null) {
                    snapshot()[position]?.isFavorite = animeDb.isFavorite
                    snapshot()[position]?.watchStatus = animeDb.watchStatus
                }
                holder.bind(item, position)
            }
        }
    }

    inner class ViewHolder(private val binding: ItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime, position: Int) {
            binding.run {
                if (anime.isFavorite == true) {
                    binding.btnFavorite.setImage(R.drawable.ic_bookmark_filled)
                } else {
                    binding.btnFavorite.setImage(R.drawable.ic_bookmark_border)
                }

                when (anime.watchStatus) {
                    WATCHING -> btnStatus.setImage(R.drawable.ic_time)
                    PLANNED -> btnStatus.setImage(R.drawable.ic_calendar_item)
                    COMPLETED -> btnStatus.setImage(R.drawable.ic_done)
                    DROPPED -> btnStatus.setImage(R.drawable.ic_delete)
                    REWATCHING -> btnStatus.setImage(R.drawable.ic_more_time)
                    HOLD -> btnStatus.setImage(R.drawable.ic_pause)
                    null -> btnStatus.setImage(R.drawable.ic_add)
                }

                poster.setImage(anime.image.original)
                name.setText(anime.russian, anime.name)
                when (type) {
                    DEFAULT -> {
                        date.setYear(anime.airedOn?.getYear())
                        episodes.hide()
                        rate.setDouble(anime.score)
                    }
                    ANONS -> {
                        date.setDate(anime.airedOn)
                        episodes.hide()
                        rate.hide()
                    }
                    ONGOING -> {
                        date.hide()
                        episodes.setEpisodes(anime.episodesAired, anime.episodes)
                        rate.setDouble(anime.score)
                    }
                }

                itemView.setOnClickListener {
                    viewModel.saveAnime(anime)
                    itemView.findNavController().navigate(R.id.animeFragment)
                }

                btnFavorite.setOnClickListener {
                    if (anime.watchStatus != null) {
                        snapshot()[position]?.isFavorite = anime.isFavorite
                        viewModel.update(anime)
                    } else {
                        if (anime.isFavorite == true) {
                            snapshot()[position]?.isFavorite = false
                            viewModel.delete(anime)
                        } else {
                            snapshot()[position]?.isFavorite = true
                            viewModel.insert(anime)
                        }
                    }
                    notifyItemChanged(position)
                }

                btnStatus.setOnClickListener {
                    val checkedItem = anime.watchStatus?.ordinal?.plus(1) ?: 0
                    MaterialAlertDialogBuilder(itemView.context)
                        .setTitle("Статус просмотра")
                        .setSingleChoiceItems(WatchStatus.titles(), checkedItem, null)
                        .setPositiveButton("Ок") { dialog, _ ->
                            val index = (dialog as AlertDialog).listView.checkedItemPosition
                            if (index != checkedItem) {
                                if (anime.isFavorite == true) {
                                    snapshot()[position]?.watchStatus =
                                        WatchStatus.getItem(index - 1)
                                    viewModel.update(anime)
                                } else {
                                    if (index == 0) {
                                        snapshot()[position]?.watchStatus = null
                                        viewModel.delete(anime)
                                    } else {
                                        snapshot()[position]?.watchStatus =
                                            WatchStatus.getItem(index - 1)
                                        viewModel.insert(anime)
                                    }
                                }
                            }
                            notifyItemChanged(position)
                        }
                        .setNegativeButton("Отмена", null)
                        .show()
                }
            }
        }

        private fun TextView.setText(primary: String, secondary: String) {
            this.text = if (primary.isEmpty()) secondary else primary
        }

        private fun TextView.setYear(date: String?) {
            if (date.isNullOrEmpty()) isVisible = false else text = date
        }

        private fun String.getYear() = this.substringBefore("-")

        private fun TextView.setDate(date: String?) {
            text = if (date.isNullOrEmpty()) {
                "Дата выхода неизвестна"
            } else {
                val networkFormatter = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
                val appFormatter = SimpleDateFormat("dd MMMM y", Locale("ru"))
                appFormatter.format(networkFormatter.parse(date)!!)
            }
        }

        private fun TextView.setEpisodes(aired: Int, total: Int) {
            text = String.format("%s из %s эпизодов", aired.toString(), total.toFormatString())
        }

        private fun Int.toFormatString(): String = if (this == 0) "?" else this.toString()

        private fun TextView.setDouble(number: Double) {
            if (number == 0.0) isVisible = false else text = number.toString()
        }
    }
}

private fun ImageButton.setImage(@DrawableRes id: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, id))
}

private fun DialogInterface.getItem(): WatchStatus {
    return WatchStatus.getItem((this as AlertDialog).listView.checkedItemPosition)
}
