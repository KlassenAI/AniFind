package com.android.anifind.presentation.ui.overview

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentRecentBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.extensions.*
import com.android.anifind.presentation.adapter.AnimeAdapter
import com.android.anifind.presentation.viewmodel.OverviewViewModel
import com.android.anifind.presentation.viewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RecentFragment: Fragment(R.layout.fragment_recent), AnimeAdapter.Listener {

    private val adapter = AnimeAdapter(this)
    private val binding: FragmentRecentBinding by viewBinding()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val overviewViewModel: OverviewViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclers()
        initButtons()
        initObservers()
    }

    private fun initRecyclers() {
        binding.recycler.init(adapter)
    }

    private fun initButtons() = with(binding) {
        btnBack.setOnClickListener { navigateUp() }
        btnClear.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Удаление недавних аниме")
                .setMessage("Вы точно хотите удалить их?")
                .setPositiveButton("Да") { _, _ ->
                    overviewViewModel.deleteRecentAnime()
                    navigateUp()
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }

    private fun initObservers() = with(binding) {
        overviewViewModel.recentAnimes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            progressBar.conceal()
            emptyMessage.isVisible = it.isEmpty()
            recycler.isVisible = it.isNotEmpty()
        }
    }

    override fun onItemClick(item: AnimeEntity) {
        sharedViewModel.initOverviewAnime(Anime(item, true))
        navigateToOverviewAnime()
    }

    override fun onFavoriteButtonClick(item: AnimeEntity, position: Int) {
        changeFavoriteParam(item, position)
        val message = getFavoriteChangeMessage(item.isFavorite)
        showSnackbar(message) { changeFavoriteParam(item, position) }
    }

    private fun changeFavoriteParam(item: AnimeEntity, position: Int) {
        item.isFavorite = item.isFavorite != true
        overviewViewModel.update(item)
        adapter.notifyItemChanged(position, item.isFavorite)
    }

    override fun onStatusButtonClick(item: AnimeEntity, position: Int) {
        val old = item.watchStatus
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(item.name)
            .setSingleChoiceItems(WatchStatus.titles(), old.ordinal) { dialog, which ->
                if (which == old.ordinal) return@setSingleChoiceItems
                changeStatusParam(item, WatchStatus.getItem(which), position)
                val message = getStatusChangeMessage(old, item.watchStatus)
                showSnackbar(message) { changeStatusParam(item, old, position) }
                dialog.dismiss()
            }
            .show()
    }

    private fun changeStatusParam(item: AnimeEntity, new: WatchStatus, position: Int) {
        item.watchStatus = new
        overviewViewModel.update(item)
        adapter.notifyItemChanged(position, item.watchStatus)
    }
}