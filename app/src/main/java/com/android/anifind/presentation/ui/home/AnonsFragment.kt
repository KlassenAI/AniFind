package com.android.anifind.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentAnonsBinding
import com.android.anifind.extensions.init
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.HomeViewModel

class AnonsFragment : BaseHomeFragment(R.layout.fragment_anons) {

    private val binding: FragmentAnonsBinding by viewBinding()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: AnimePagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.requestAnons()
        adapter = AnimePagingAdapter(homeViewModel, this@AnonsFragment)
        recycler.init(adapter, progressBar, errorMessage)
        btnRetry.setOnClickListener { adapter.retry() }
        homeViewModel.anons.observe(viewLifecycleOwner) { adapter.submitData(lifecycle, it) }
    }
}