package com.android.anifind.presentation.ui.overview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentPopularBinding
import com.android.anifind.extensions.init
import com.android.anifind.extensions.navigateUp
import com.android.anifind.presentation.adapter.AdapterType.DEFAULT
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.OverviewViewModel

class PopularFragment : Fragment(R.layout.fragment_popular) {

    private val binding: FragmentPopularBinding by viewBinding()
    private val overviewViewModel: OverviewViewModel by activityViewModels()
    private lateinit var adapter: AnimePagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        overviewViewModel.requestPopularAnimes()
        adapter = AnimePagingAdapter(DEFAULT, overviewViewModel, this@PopularFragment)
        recycler.init(adapter, progressBar, errorMessage)
        btnBack.setOnClickListener { navigateUp() }
        btnRetry.setOnClickListener { adapter.retry() }
        overviewViewModel.popularAnimes.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
    }
}