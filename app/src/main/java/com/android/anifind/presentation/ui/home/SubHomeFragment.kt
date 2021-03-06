package com.android.anifind.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentSubHomeBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.extensions.*
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.HomeViewModel
import com.android.anifind.presentation.viewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class SubHomeFragment : Fragment(R.layout.fragment_sub_home), AnimePagingAdapter.Listener {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val binding: FragmentSubHomeBinding by viewBinding()
    private val adapter = AnimePagingAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initButtons()
        initData()
        initObserver()
    }

    private fun initRecycler() = with(binding) { recycler.init(adapter, progressBar, errorMessage) }

    private fun initButtons() = with(binding) { btnRetry.setOnClickListener { adapter.retry() } }

    private fun initData() {
        when (this) {
            is AnonsFragment -> homeViewModel.requestAnons()
            is LatestFragment -> homeViewModel.requestLatest()
            is OngoingFragment -> homeViewModel.requestOngoings()
        }
    }

    private fun initObserver() {
        when (this) {
            is AnonsFragment -> homeViewModel.anons.observe()
            is LatestFragment -> homeViewModel.latest.observe()
            is OngoingFragment -> homeViewModel.ongoings.observe()
        }
    }

    private fun LiveData<PagingData<Anime>>.observe() = observe(viewLifecycleOwner) {
        adapter.submitData(lifecycle, it)
    }

    override fun onItemClick(item: Anime) {
        sharedViewModel.initHomeAnime(item)
        navigateToHomeAnime()
    }

    override fun onFavoriteButtonClick(item: Anime, position: Int) {
        insertAnime(item)
        changeFavoriteParam(item.entity, position)
        val message = getFavoriteChangeMessage(item.entity.isFavorite)
        showSnackbar(message) { changeFavoriteParam(item.entity, position) }
    }

    private fun insertAnime(item: Anime) {
        if (!item.saved) {
            homeViewModel.insert(item.entity)
            item.saved = true
        }
    }

    private fun changeFavoriteParam(item: AnimeEntity, position: Int) {
        item.isFavorite = item.isFavorite != true
        homeViewModel.update(item)
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
        homeViewModel.update(item)
        adapter.notifyItemChanged(position, item.watchStatus)
    }

    override fun onLoadAnimeFromDB(item: Anime, position: Int) {
        homeViewModel.getAnime(item.entity.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                item.saved = true
                item.entity.addDate = it.addDate
                item.entity.updateDate = it.updateDate
                if (it.info == null) homeViewModel.requestAnimeInfo(item.entity.id)
                else item.entity.info = it.info
                item.entity.isFavorite = it.isFavorite
                adapter.notifyItemChanged(position, item.entity.isFavorite)
                item.entity.watchStatus = it.watchStatus
                adapter.notifyItemChanged(position, item.entity.watchStatus)
            }
    }
}