package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.databinding.ItemAnimeBinding
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.domain.model.WatchStatus.NO
import com.android.anifind.extensions.navigateToAnimeFragment
import com.android.anifind.presentation.viewmodel.BaseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.schedulers.Schedulers

class AnimeAdapter(
    private val viewModel: BaseViewModel,
    private val fragment: Fragment,
) : RecyclerView.Adapter<AnimeAdapter.ViewHolder>() {

    fun submitList(list: List<AnimeEntity>) = differ.submitList(list)

    private fun getItem(position: Int) = differ.currentList[position]

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<AnimeEntity>() {
        override fun areItemsTheSame(old: AnimeEntity, new: AnimeEntity): Boolean = old.id == new.id
        override fun areContentsTheSame(old: AnimeEntity, new: AnimeEntity): Boolean = old == new
    })

    override fun getItemCount(): Int = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item.info == null) loadAnimeInfo(item)
        holder.bind(getItem(position), position)
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
            itemView.setOnClickListener { navigateToAnimeFragment(entity) }
            btnFavorite.setOnClickListener { onFavoriteBtnClick(entity, position) }
            btnStatus.setOnClickListener { onStatusBtnClick(entity, position) }
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
            fragment.view?.snackbar(text) { changeFavoriteParam(entity, position) }
        }

        private fun changeFavoriteParam(animeEntity: AnimeEntity, position: Int) {
            animeEntity.isFavorite = animeEntity.isFavorite != true
            when {
                animeEntity.watchStatus != NO -> viewModel.update(animeEntity)
                animeEntity.isFavorite -> viewModel.insert(animeEntity)
                else -> viewModel.delete(animeEntity)
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
                    fragment.view?.snackbar(text) { changeStatusParam(entity, index, old, new) }
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
            notifyItemChanged(position, PayloadType.STATUS)
        }
    }
}
