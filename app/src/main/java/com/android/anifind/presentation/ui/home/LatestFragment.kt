package com.android.anifind.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentLatestBinding
import com.android.anifind.extensions.init
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.HomeViewModel

class LatestFragment : BaseHomeFragment(R.layout.fragment_latest) {

    private val binding: FragmentLatestBinding by viewBinding()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: AnimePagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AnimePagingAdapter(homeViewModel, this@LatestFragment)
        recycler.init(adapter, progressBar, errorMessage)
        btnRetry.setOnClickListener { adapter.retry() }
        homeViewModel.requestLatest()
        homeViewModel.latest.observe(viewLifecycleOwner) { adapter.submitData(lifecycle, it) }
    }
}