package com.android.anifind.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.databinding.ItemAnimeBinding
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.domain.model.WatchStatus.*
import com.android.anifind.extensions.navigateToAnimeFragment
import com.android.anifind.presentation.adapter.AdapterType.*
import com.android.anifind.presentation.adapter.PayloadType.FAVORITE
import com.android.anifind.presentation.adapter.PayloadType.STATUS
import com.android.anifind.presentation.viewmodel.BaseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AnimePagingAdapter(
    private val type: AdapterType,
    private val viewModel: BaseViewModel,
    private val fragment: Fragment,
) : PagingDataAdapter<AnimeEntity, AnimePagingAdapter.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<AnimeEntity>() {
        override fun areItemsTheSame(old: AnimeEntity, new: AnimeEntity) = old.id == new.id
        override fun areContentsTheSame(old: AnimeEntity, new: AnimeEntity) = old == new
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val anime = getItem(position) ?: return
        holder.bind(anime, position)
        viewModel.getAnime(anime.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it == null) return@subscribe
                anime.addDate = it.addDate
                anime.updateDate = it.updateDate
                if (it.info == null) loadAnimeInfo(anime)
                else anime.info = it.info
                anime.isFavorite = it.isFavorite
                notifyItemChanged(position, FAVORITE)
                anime.watchStatus = it.watchStatus
                notifyItemChanged(position, STATUS)
            }
    }

    private fun loadAnimeInfo(animeEntity: AnimeEntity) {
        viewModel.requestAnimeInfo(animeEntity.id)
            .subscribeOn(Schedulers.io())
            .subscribe({ viewModel.update(animeEntity.apply { info = it }) }, {})
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            payloads.forEach { payload ->
                val item = getItem(position)
                when (payload) {
                    FAVORITE -> item?.let { holder.changeFavoriteButtonDraw(it) }
                    STATUS -> item?.let { holder.changeStatusButtonDraw(it) }
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

        fun bind(entity: AnimeEntity, position: Int) = with(binding) {
            Log.d("entity", entity.toString())
            changeFavoriteButtonDraw(entity)
            changeStatusButtonDraw(entity)
            poster.setImage(entity.imageUrl)
            name.text = entity.name
            when (type) {
                DEFAULT -> bindByType(date = entity.year, score = entity.score)
                ANONS -> bindByType(date = entity.date)
                ONGOING -> bindByType(eps = entity.episodesInfo, score = entity.score)
            }
            itemView.setOnClickListener { navigateToAnimeFragment(entity) }
            btnFavorite.setOnClickListener { onFavoriteBtnClick(entity, position) }
            btnStatus.setOnClickListener { onStatusBtnClick(entity, position) }
        }

        private fun bindByType(
            date: String = "", eps: String = "", score: String = ""
        ) = with(binding) {
            if (date.isEmpty()) this.date.hide() else this.date.text = date
            if (eps.isEmpty()) this.episodes.hide() else this.episodes.text = eps
            if (score.isEmpty()) this.score.hide() else this.score.text = score
        }

        private fun navigateToAnimeFragment(entity: AnimeEntity) {
            viewModel.saveAnime(entity)
            fragment.navigateToAnimeFragment()
        }

        private fun onFavoriteBtnClick(entity: AnimeEntity, position: Int) {
            changeFavoriteParam(entity, position)
            val text = when (entity.isFavorite) {
                true -> "Добавлено в избранное"
                else -> "Удалено из избранного"
            }
            showSnackbarWithAction(text) { changeFavoriteParam(entity, position) }
        }

        private fun changeFavoriteParam(animeEntity: AnimeEntity, position: Int) {
            animeEntity.isFavorite = animeEntity.isFavorite != true
            when {
                animeEntity.watchStatus != NO -> viewModel.update(animeEntity)
                animeEntity.isFavorite -> viewModel.insert(animeEntity)
                else -> viewModel.delete(animeEntity)
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
                    showSnackbarWithAction(text) { changeStatusParam(entity, index, old, new) }
                    dialog.dismiss()
                }
                .setNegativeButton("Отмена", null)
                .show()
        }

        private fun changeStatusParam(animeEntity: AnimeEntity, position: Int, new: Int, old: Int) {
            animeEntity.watchStatus = WatchStatus.getItem(new)
            when {
                animeEntity.isFavorite || old != 0 -> viewModel.update(animeEntity)
                animeEntity.watchStatus == NO -> viewModel.delete(animeEntity)
                else -> viewModel.insert(animeEntity)
            }
            notifyItemChanged(position, STATUS)
        }

        private fun showSnackbarWithAction(text: String, action: () -> Unit) = Snackbar
            .make(fragment.view!!, text, Snackbar.LENGTH_SHORT)
            .setAction("Отмена") { action() }
            .show()
    }
}