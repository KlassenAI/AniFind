package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.databinding.ItemAnimeBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.domain.model.WatchStatus.NO
import com.android.anifind.presentation.viewmodel.BaseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.schedulers.Schedulers

class AnimeEntityAdapter(
    private val baseViewModel: BaseViewModel,
    private val listener: OnItemClickListener,
) : GenericAdapter<AnimeEntity, AnimeEntityAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun notifyItemClicked(animeEntity: AnimeEntity)
        fun notifyShowSnackbar(text: String, action: () -> Unit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item.info == null) loadAnimeInfo(item)
        holder.bind(getItem(position), position)
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
                    PayloadType.FAVORITE -> item.let { holder.bindFavoriteButton(it) }
                    PayloadType.STATUS -> item.let { holder.bindStatusButton(it) }
                }
            }
        } else super.onBindViewHolder(holder, position, payloads)
    }

    inner class ViewHolder(val binding: ItemAnimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindFavoriteButton(animeEntity: AnimeEntity) = with(binding) {
            changeFavoriteButtonDraw(btnFavorite, animeEntity.isFavorite)
        }

        fun bindStatusButton(animeEntity: AnimeEntity) = with(binding) {
            changeStatusButtonDraw(btnStatus, animeEntity.watchStatus)
        }

        fun bind(entity: AnimeEntity, position: Int) = with(binding) {
            bindFavoriteButton(entity)
            bindStatusButton(entity)
            poster.setImage(entity.imageUrl)
            name.text = entity.name
            date.text = entity.year
            episodes.hide()
            score.text = entity.score
            itemView.setOnClickListener { listener.notifyItemClicked(entity) }
            btnFavorite.setOnClickListener { onFavoriteBtnClick(entity, position) }
            btnStatus.setOnClickListener { onStatusBtnClick(entity, position) }
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
            notifyItemChanged(position, PayloadType.FAVORITE)
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
            notifyItemChanged(position, PayloadType.STATUS)
        }
    }
}