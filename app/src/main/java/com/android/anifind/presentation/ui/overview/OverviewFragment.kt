package com.android.anifind.presentation.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.anifind.databinding.FragmentOverviewBinding
import com.android.anifind.extensions.navigateToFilter
import com.android.anifind.extensions.navigateToPopular
import com.android.anifind.extensions.navigateToSearch
import com.android.anifind.presentation.viewmodel.OverviewViewModel

class OverviewFragment : Fragment() {

    private val viewModel by activityViewModels<OverviewViewModel>()
    private lateinit var binding: FragmentOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOverviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnSearch.setOnClickListener { navigateToSearch() }
            btnFilter.setOnClickListener {
                viewModel.setFilterChanging()
                navigateToFilter()
            }
            btnPopular.setOnClickListener { navigateToPopular() }
        }
    }
}