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
import com.android.anifind.presentation.adapter.AdapterType.*
import com.android.anifind.presentation.adapter.PayloadType.FAVORITE
import com.android.anifind.presentation.adapter.PayloadType.STATUS
import com.android.anifind.presentation.viewmodel.BaseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AnimePagingAdapter(
    private val baseViewModel: BaseViewModel,
    private val listener: OnItemClickListener
) : PagingDataAdapter<Anime, AnimePagingAdapter.ViewHolder>(DiffCallback) {

    interface OnItemClickListener {
        fun notifyItemClicked(anime: Anime)
        fun notifyShowSnackbar(text: String, action: () -> Unit)
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
        baseViewModel.getAnime(anime.entity.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                anime.saved = true
                anime.entity.addDate = it.addDate
                anime.entity.updateDate = it.updateDate
                if (it.info == null) loadAnimeInfo(anime.entity)
                else anime.entity.info = it.info
                anime.entity.isFavorite = it.isFavorite
                notifyItemChanged(position, FAVORITE)
                anime.entity.watchStatus = it.watchStatus
                notifyItemChanged(position, STATUS)
            }
    }

    private fun loadAnimeInfo(animeEntity: AnimeEntity) {
        baseViewModel.requestAnimeInfo(animeEntity.id)
            .subscribeOn(Schedulers.io())
            .subscribe({ baseViewModel.update(animeEntity.apply { info = it }) }, {})
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            payloads.forEach { payload ->
                val item = getItem(position)
                when (payload) {
                    FAVORITE -> item?.let { holder.changeFavoriteButtonDraw(it.entity) }
                    STATUS -> item?.let { holder.changeStatusButtonDraw(it.entity) }
                }
            }
        } else super.onBindViewHolder(holder, position, payloads)
    }

    inner class ViewHolder(private val binding: ItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun changeFavoriteButtonDraw(animeEntity: AnimeEntity) = with(binding) {
            changeFavoriteButtonDraw(btnFavorite, animeEntity.isFavorite)
        }

        fun changeStatusButtonDraw(animeEntity: AnimeEntity) = with(binding) {
            changeStatusButtonDraw(btnStatus, animeEntity.watchStatus)
        }

        fun bind(anime: Anime, position: Int) = with(binding) {
            changeFavoriteButtonDraw(anime.entity)
            changeStatusButtonDraw(anime.entity)
            poster.setImage(anime.entity.imageUrl)
            name.text = anime.entity.name
            bindByType(date = anime.entity.year, score = anime.entity.score)
            itemView.setOnClickListener { listener.notifyItemClicked(anime) }
            btnFavorite.setOnClickListener { listener.notifyItemClicked(anime) }
            btnStatus.setOnClickListener { onStatusBtnClick(anime.entity, position) }
        }

        private fun bindByType(
            date: String = "", eps: String = "", score: String = ""
        ) = with(binding) {
            if (date.isEmpty()) this.date.hide() else this.date.text = date
            if (eps.isEmpty()) this.episodes.hide() else this.episodes.text = eps
            if (score.isEmpty()) this.score.hide() else this.score.text = score
        }

        private fun onFavoriteBtnClick(entity: AnimeEntity, position: Int) {
            changeFavoriteParam(entity, position)
            val text = when (entity.isFavorite) {
                true -> "Добавлено в избранное"
                else -> "Удалено из избранного"
            }
            listener.notifyShowSnackbar(text) { changeFavoriteParam(entity, position) }
        }

        private fun changeFavoriteParam(animeEntity: AnimeEntity, position: Int) {
            animeEntity.isFavorite = animeEntity.isFavorite != true
            when {
                animeEntity.watchStatus != NO -> baseViewModel.update(animeEntity)
                animeEntity.isFavorite -> baseViewModel.insert(animeEntity)
                else -> baseViewModel.delete(animeEntity)
            }
            notifyItemChanged(position, FAVORITE)
        }

        private fun onStatusBtnClick(entity: AnimeEntity, index: Int) {
            val old = entity.watchStatus.ordinal
            MaterialAlertDialogBuilder(itemView.context)
                .setTitle(entity.name)
                .setSingleChoiceItems(WatchStatus.titles(), old) { dialog, new ->
                    if (new == old) return@setSingleChoiceItems
                    changeStatusParam(entity, index, new, old)
                    val text = when (new) {
                        0 -> "Удалено из ${
                            when (old) {
                                1 -> "просматриваемого"
                                2 -> "запланированного"
                                3 -> "просмотренного"
                                4 -> "отложенного"
                                5 -> "брошенного"
                                else -> throw Exception()
                            }
                        }"
                        1 -> "Добавлено в просматриваемое"
                        2 -> "Добавлено в запланированное"
                        3 -> "Добавлено в просмотренное"
                        4 -> "Добавлено в отложенное"
                        5 -> "Добавлено в брошенное"
                        else -> throw Exception()
                    }
                    listener.notifyShowSnackbar(text) { changeStatusParam(entity, index, old, new) }
                    dialog.dismiss()
                }
                .setNegativeButton("Отмена", null)
                .show()
        }

        private fun changeStatusParam(animeEntity: AnimeEntity, position: Int, new: Int, old: Int) {
            animeEntity.watchStatus = WatchStatus.getItem(new)
            when {
                animeEntity.isFavorite || old != 0 -> baseViewModel.update(animeEntity)
                animeEntity.watchStatus == NO -> baseViewModel.delete(animeEntity)
                else -> baseViewModel.insert(animeEntity)
            }
            notifyItemChanged(position, STATUS)
        }
    }
}