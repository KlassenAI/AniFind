package com.android.anifind.presentation.ui.overview

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentRecentBinding
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.extensions.*
import com.android.anifind.presentation.adapter.AnimeEntityAdapter
import com.android.anifind.presentation.viewmodel.OverviewViewModel
import com.android.anifind.presentation.viewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RecentFragment: Fragment(R.layout.fragment_recent), AnimeEntityAdapter.OnItemClickListener {

    private val binding: FragmentRecentBinding by viewBinding()
    private val viewModel: OverviewViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var entityAdapter: AnimeEntityAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        entityAdapter = AnimeEntityAdapter(viewModel, this)
        initRecyclers()
        initButtons()
        initObservers()
    }

    private fun initRecyclers() = with(binding) { recycler.init(entityAdapter) }

    private fun initButtons() = with(binding) {
        btnBack.setOnClickListener { navigateUp() }
        btnClear.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Удаление недавних аниме")
                .setMessage("Вы точно хотите удалить их?")
                .setPositiveButton("Да") { _, _ ->
                    viewModel.deleteRecentAnime()
                    navigateUp()
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }

    private fun initObservers() = with(binding) {
        viewModel.recentAnimes.observe(viewLifecycleOwner) {
            entityAdapter.submitList(it)
            progressBar.conceal()
            emptyMessage.isVisible = it.isEmpty()
            recycler.isVisible = it.isNotEmpty()
        }
    }

    override fun notifyItemClicked(animeEntity: AnimeEntity) {
        sharedViewModel.initBookmarksAnime(animeEntity)
        navigateToBookmarksAnime()
    }

    override fun notifyShowSnackbar(text: String, action: () -> Unit) = showSnackbar(text, action)
}