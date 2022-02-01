package com.android.anifind.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentOngoingBinding
import com.android.anifind.extensions.init
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.HomeViewModel

class OngoingFragment : BaseHomeFragment(R.layout.fragment_ongoing) {

    private val binding: FragmentOngoingBinding by viewBinding()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: AnimePagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.requestOnGoings()
        adapter = AnimePagingAdapter(homeViewModel, this@OngoingFragment)
        recycler.init(adapter, progressBar, errorMessage)
        btnRetry.setOnClickListener { adapter.retry() }
        homeViewModel.ongoings.observe(viewLifecycleOwner) { adapter.submitData(lifecycle, it) }
    }
}