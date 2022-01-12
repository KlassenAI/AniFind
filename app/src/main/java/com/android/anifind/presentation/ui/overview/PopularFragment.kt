package com.android.anifind.presentation.ui.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.android.anifind.databinding.FragmentPopularBinding
import com.android.anifind.extensions.init
import com.android.anifind.extensions.navigateUp
import com.android.anifind.presentation.adapter.AdapterType.DEFAULT
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.AnimeViewModel
import com.android.anifind.presentation.viewmodel.OverviewViewModel

class PopularFragment : Fragment() {

    private val adapter = AnimePagingAdapter(DEFAULT)
    private val animeViewModel: AnimeViewModel by activityViewModels()
    private val overviewViewModel: OverviewViewModel by activityViewModels()
    private lateinit var binding: FragmentPopularBinding

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        binding = FragmentPopularBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overviewViewModel.requestPopularAnimes()
        binding.apply {
            recycler.init(adapter, animeViewModel, progressBar, errorMessage)
            btnBack.setOnClickListener { navigateUp() }
            btnRetry.setOnClickListener { adapter.retry() }
        }
        overviewViewModel.popularAnimes.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
    }
}