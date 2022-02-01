package com.android.anifind.presentation.ui.overview

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentOverviewBinding
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.extensions.initHorizontal
import com.android.anifind.extensions.navigateToFilter
import com.android.anifind.extensions.navigateToRecent
import com.android.anifind.extensions.navigateToSearch
import com.android.anifind.presentation.adapter.AnimeCardAdapter
import com.android.anifind.presentation.adapter.hide
import com.android.anifind.presentation.viewmodel.OverviewViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class OverviewFragment : Fragment(R.layout.fragment_overview),
    AnimeCardAdapter.OnItemClickListener {

    private val adapter = AnimeCardAdapter(this)
    private val binding: FragmentOverviewBinding by viewBinding()
    private val viewModel: OverviewViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclers()
        initObservers()
        initButtons()
    }

    private fun initRecyclers() = with(binding) {
        recyclerRecent.initHorizontal(adapter)
    }

    private fun initObservers() = with(binding) {
        viewModel.recentAnimes.observe(viewLifecycleOwner) { list ->
            list.isNotEmpty().let {
                layoutRecent.isVisible = it
                if (it) adapter.submitList(list)
            }
        }
    }

    private fun initButtons() = with(binding) {
        btnShowRecentList.setOnClickListener { navigateToRecent() }
        btnSearch.setOnClickListener { navigateToSearch() }
        btnFilter.setOnClickListener {
            viewModel.setFilterChanging()
            navigateToFilter()
        }
    }

    override fun notifyItemClicked(animeEntity: AnimeEntity) {
        Toast.makeText(requireContext(), "$animeEntity", Toast.LENGTH_SHORT).show()
    }
}