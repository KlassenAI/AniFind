package com.android.anifind.presentation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.anifind.databinding.FragmentLatestBinding
import com.android.anifind.extensions.init
import com.android.anifind.presentation.adapter.AdapterType.DEFAULT
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.AnimeViewModel
import com.android.anifind.presentation.viewmodel.HomeViewModel

class LatestFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val animeViewModel: AnimeViewModel by activityViewModels()
    private lateinit var adapter: AnimePagingAdapter
    private lateinit var binding: FragmentLatestBinding

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        binding = FragmentLatestBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AnimePagingAdapter(DEFAULT, homeViewModel, this)
        binding.apply {
            recycler.init(adapter, animeViewModel, progressBar, errorMessage)
            btnRetry.setOnClickListener { adapter.retry() }
        }
        homeViewModel.latest.observe(viewLifecycleOwner) { adapter.submitData(lifecycle, it) }
    }
}
