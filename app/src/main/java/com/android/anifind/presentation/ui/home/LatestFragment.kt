package com.android.anifind.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentLatestBinding
import com.android.anifind.extensions.init
import com.android.anifind.presentation.adapter.AdapterType.DEFAULT
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.HomeViewModel

class LatestFragment : Fragment(R.layout.fragment_latest) {

    private val binding: FragmentLatestBinding by viewBinding()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: AnimePagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.requestLatest()
        adapter = AnimePagingAdapter(DEFAULT, homeViewModel, this@LatestFragment)
        recycler.init(adapter, progressBar, errorMessage)
        btnRetry.setOnClickListener { adapter.retry() }
        homeViewModel.latest.observe(viewLifecycleOwner) { adapter.submitData(lifecycle, it) }
    }
}