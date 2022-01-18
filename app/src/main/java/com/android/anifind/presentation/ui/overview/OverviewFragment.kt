package com.android.anifind.presentation.ui.overview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentOverviewBinding
import com.android.anifind.extensions.navigateToFilter
import com.android.anifind.extensions.navigateToPopular
import com.android.anifind.extensions.navigateToSearch
import com.android.anifind.presentation.viewmodel.OverviewViewModel

class OverviewFragment : Fragment(R.layout.fragment_overview) {

    private val viewModel by activityViewModels<OverviewViewModel>()
    private val binding: FragmentOverviewBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        btnSearch.setOnClickListener { navigateToSearch() }
        btnFilter.setOnClickListener {
            viewModel.setFilterChanging()
            navigateToFilter()
        }
        btnPopular.setOnClickListener { navigateToPopular() }
    }
}