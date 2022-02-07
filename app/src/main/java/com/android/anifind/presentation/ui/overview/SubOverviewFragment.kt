package com.android.anifind.presentation.ui.overview

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.extensions.*
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.OverviewViewModel
import com.android.anifind.presentation.viewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

open class SubOverviewFragment(@LayoutRes id: Int) : Fragment(id), AnimePagingAdapter.Listener {

    val adapter = AnimePagingAdapter(this)
    private val overviewViewModel: OverviewViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onItemClick(item: Anime) {
        sharedViewModel.initOverviewAnime(item)
        navigateToOverviewAnime()
    }

    override fun onFavoriteButtonClick(item: Anime, position: Int) {
        insertAnime(item)
        changeFavoriteParam(item.entity, position)
        val message = getFavoriteChangeMessage(item.entity.isFavorite)
        showSnackbar(message) { changeFavoriteParam(item.entity, position) }
    }

    private fun insertAnime(item: Anime) {
        if (!item.saved) {
            overviewViewModel.insert(item.entity)
            item.saved = true
        }
    }

    private fun changeFavoriteParam(item: AnimeEntity, position: Int) {
        item.isFavorite = item.isFavorite != true
        overviewViewModel.update(item)
        adapter.notifyItemChanged(position, item.isFavorite)
    }

    override fun onStatusButtonClick(item: Anime, position: Int) {
        val old = item.entity.watchStatus
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(item.entity.name)
            .setSingleChoiceItems(WatchStatus.titles(), old.ordinal) { dialog, which ->
                if (which == old.ordinal) return@setSingleChoiceItems
                insertAnime(item)
                changeStatusParam(item.entity, WatchStatus.getItem(which), position)
                val message = getStatusChangeMessage(old, item.entity.watchStatus)
                showSnackbar(message) { changeStatusParam(item.entity, old, position) }
                dialog.dismiss()
            }
            .show()
    }

    private fun changeStatusParam(item: AnimeEntity, new: WatchStatus, position: Int) {
        item.watchStatus = new
        overviewViewModel.update(item)
        adapter.notifyItemChanged(position, item.watchStatus)
    }

    override fun onLoadAnimeFromDB(item: Anime, position: Int) {
        overviewViewModel.getAnime(item.entity.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                item.saved = true
                item.entity.addDate = it.addDate
                item.entity.updateDate = it.updateDate
                if (it.info == null) overviewViewModel.requestAnimeInfo(item.entity.id)
                else item.entity.info = it.info
                item.entity.isFavorite = it.isFavorite
                adapter.notifyItemChanged(position, item.entity.isFavorite)
                item.entity.watchStatus = it.watchStatus
                adapter.notifyItemChanged(position, item.entity.watchStatus)
            }
    }
}