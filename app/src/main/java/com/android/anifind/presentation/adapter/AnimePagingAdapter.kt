package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.R
import com.android.anifind.databinding.ItemAnimeBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.domain.model.WatchStatus.*
import com.android.anifind.extensions.hide
import com.android.anifind.extensions.navigateToAnime
import com.android.anifind.extensions.setImage
import com.android.anifind.presentation.adapter.AdapterType.*
import com.android.anifind.presentation.adapter.AnimePagingAdapter.BindType.FAVORITE
import com.android.anifind.presentation.adapter.AnimePagingAdapter.BindType.STATUS
import com.android.anifind.presentation.viewmodel.BaseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AnimePagingAdapter(
    private val type: AdapterType,
    private val viewModel: BaseViewModel,
    private val fragment: Fragment,
) : PagingDataAdapter<Anime, AnimePagingAdapter.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(old: Anime, new: Anime): Boolean = old.id == new.id
        override fun areContentsTheSame(old: Anime, new: Anime): Boolean = old == new
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*
        getItem(position)?.let { item ->
            viewModel.getAnime(item.id).observe(fragment) { animeDb ->
                item.isFavorite = animeDb?.isFavorite
                item.watchStatus = animeDb?.watchStatus
                holder.bind(item)
            }
        }
         */
        getItem(position)?.let { anime ->
            viewModel.getAnime(anime.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    anime.isFavorite = it?.isFavorite
                    notifyItemChanged(position, FAVORITE)
                    anime.watchStatus = it?.watchStatus
                    notifyItemChanged(position, STATUS)
                }
        }
        getItem(position)?.let { holder.bind(it, position) }
    }

    enum class BindType {
        FAVORITE,
        STATUS
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            payloads.forEach { payload ->
                when (payload) {
                    FAVORITE -> getItem(position)?.let {
                        holder.binding.btnFavorite.setDraw(
                            when (it.isFavorite) {
                                true -> R.drawable.ic_bookmark_filled
                                else -> R.drawable.ic_bookmark_border
                            }
                        )
                    }
                    STATUS -> getItem(position)?.let {
                        holder.binding.btnStatus.setDraw(
                            when (it.watchStatus) {
                                WATCHING -> R.drawable.ic_play
                                PLANNED -> R.drawable.ic_calendar_item
                                COMPLETED -> R.drawable.ic_done
                                HOLD -> R.drawable.ic_pause
                                DROPPED -> R.drawable.ic_cancel
                                null -> R.drawable.ic_add
                            }
                        )
                    }
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class ViewHolder(val binding: ItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private fun snackbarWithAction(text: String, action: () -> Unit) {
            Snackbar.make(fragment.view!!, text, Snackbar.LENGTH_SHORT)
                .setAction("Отмена") { action() }
                .show()
        }

        private fun notifyFavoriteParamChanged(anime: Anime) = binding.btnFavorite.setDraw(
            when (anime.isFavorite) {
                true -> R.drawable.ic_bookmark_filled
                else -> R.drawable.ic_bookmark_border
            }
        )

        private fun notifyStatusParamChanged(anime: Anime) = binding.btnStatus.setDraw(
            when (anime.watchStatus) {
                WATCHING -> R.drawable.ic_play
                PLANNED -> R.drawable.ic_calendar_item
                COMPLETED -> R.drawable.ic_done
                HOLD -> R.drawable.ic_pause
                DROPPED -> R.drawable.ic_cancel
                null -> R.drawable.ic_add
            }
        )

        private fun changeFavoriteParam(anime: Anime, position: Int) {
            anime.isFavorite = anime.isFavorite != true
            when {
                anime.watchStatus != null -> viewModel.update(anime)
                anime.isFavorite == true -> viewModel.insert(anime)
                else -> viewModel.delete(anime)
            }
            notifyItemChanged(position, FAVORITE)
        }

        private fun changeStatusParam(anime: Anime, position: Int, new: Int, old: Int) {
            anime.watchStatus = WatchStatus.getItem(new - 1)
            when {
                anime.isFavorite == true || old != 0 -> viewModel.update(anime)
                new == 0 -> viewModel.delete(anime)
                else -> viewModel.insert(anime)
            }
            notifyItemChanged(position, STATUS)
        }

        fun bind(anime: Anime, position: Int) = with(binding) {
            notifyFavoriteParamChanged(anime)
            notifyStatusParamChanged(anime)
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
                fragment.navigateToAnime()
            }

            btnFavorite.setOnClickListener {
                changeFavoriteParam(anime, position)
                val text = when (anime.isFavorite) {
                    true -> "Добавлено в избранное"
                    else -> "Удалено из избранного"
                }
                snackbarWithAction(text) { changeFavoriteParam(anime, position) }
            }

            btnStatus.setOnClickListener {
                val oldIndex = anime.watchStatus?.ordinal?.plus(1) ?: 0
                MaterialAlertDialogBuilder(itemView.context)
                    .setTitle(anime.russian)
                    .setSingleChoiceItems(WatchStatus.titles(), oldIndex) { dialog, newIndex ->
                        if (newIndex != oldIndex) {
                            changeStatusParam(anime, position, newIndex, oldIndex)
                            val text = when (newIndex) {
                                0 -> {
                                    val secondaryText = when (oldIndex) {
                                        1 -> "просматриваемого"
                                        2 -> "запланированного"
                                        3 -> "просмотренного"
                                        4 -> "отложенного"
                                        5 -> "брошенного"
                                        else -> throw Exception()
                                    }
                                    "Удалено из $secondaryText"
                                }
                                1 -> "Добавлено в просматриваемое"
                                2 -> "Добавлено в запланированное"
                                3 -> "Добавлено в просмотренное"
                                4 -> "Добавлено в отложенное"
                                5 -> "Добавлено в брошенное"
                                else -> throw Exception()
                            }
                            snackbarWithAction(text) { changeStatusParam(anime, position, oldIndex, newIndex) }
                            dialog.dismiss()
                        }
                    }
                    .setNegativeButton("Отмена", null)
                    .show()

            }
        }
    }
}