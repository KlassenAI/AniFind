package com.android.anifind.presentation.ui.overview

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentOverviewBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.extensions.*
import com.android.anifind.presentation.adapter.AnimeCardAdapter
import com.android.anifind.presentation.viewmodel.OverviewViewModel
import com.android.anifind.presentation.viewmodel.SharedViewModel

class OverviewFragment : Fragment(R.layout.fragment_overview) {

    private val recentAdapter = AnimeCardAdapter()
    private val binding: FragmentOverviewBinding by viewBinding()
    private val overviewViewModel: OverviewViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initRecycler()
        initObserver()
        initButtons()
    }

    private fun initAdapter() {
        recentAdapter.setOnItemClickListener {
            sharedViewModel.initOverviewAnime(Anime(it, true))
            navigateToOverviewAnime()
        }
    }

    private fun initRecycler() {
        binding.recyclerRecent.initHorizontal(recentAdapter)
    }

    private fun initObserver() {
        overviewViewModel.recentAnimes.observe(viewLifecycleOwner) { list ->
            list.isNotEmpty().let {
                binding.layoutRecent.isVisible = it
                if (it) recentAdapter.submitList(list)
            }
        }
    }

    private fun initButtons() = with(binding) {
        btnShowRecentList.setOnClickListener { navigateToRecent() }
        btnSearch.setOnClickListener { navigateToSearch() }
        btnFilter.setOnClickListener {
            overviewViewModel.setFilterChanging()
            navigateToFilter()
        }
    }
}