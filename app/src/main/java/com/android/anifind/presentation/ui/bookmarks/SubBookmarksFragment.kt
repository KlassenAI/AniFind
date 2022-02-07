package com.android.anifind.presentation.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentSubBookmarksBinding
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.extensions.*
import com.android.anifind.presentation.adapter.AnimeAdapter
import com.android.anifind.presentation.viewmodel.BookmarksViewModel
import com.android.anifind.presentation.viewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class SubBookmarksFragment : Fragment(R.layout.fragment_sub_bookmarks),
    AnimeAdapter.Listener {

    private val viewModel: BookmarksViewModel by activityViewModels()
    private val binding: FragmentSubBookmarksBinding by viewBinding()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val adapter = AnimeAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initObserver()
    }

    private fun initRecycler() {
        binding.recycler.init(adapter)
    }

    private fun initObserver() = when(this) {
        is CompletedFragment -> viewModel.completedAnimes.observe()
        is DroppedFragment -> viewModel.droppedAnimes.observe()
        is FavoriteFragment -> viewModel.favoriteAnimes.observe()
        is HoldFragment -> viewModel.holdAnimes.observe()
        is PlannedFragment -> viewModel.plannedAnimes.observe()
        is WatchingFragment -> viewModel.watchingAnimes.observe()
        else -> {}
    }

    private fun LiveData<List<AnimeEntity>>.observe() = observe(viewLifecycleOwner) {
        adapter.submitList(it)
        binding.progressBar.conceal()
        binding.emptyMessage.isVisible = it.isEmpty()
        binding.recycler.isVisible = it.isNotEmpty()
    }

    override fun onItemClick(item: AnimeEntity) {
        sharedViewModel.initBookmarksAnime(item)
        navigateToBookmarksAnime()
    }

    override fun onFavoriteButtonClick(item: AnimeEntity, position: Int) {
        changeFavoriteParam(item, position)
        val message = getFavoriteChangeMessage(item.isFavorite)
        showSnackbar(message) { changeFavoriteParam(item, position) }
    }

    private fun changeFavoriteParam(item: AnimeEntity, position: Int) {
        item.isFavorite = item.isFavorite != true
        viewModel.update(item)
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
        viewModel.update(item)
        adapter.notifyItemChanged(position, item.watchStatus)
    }
}