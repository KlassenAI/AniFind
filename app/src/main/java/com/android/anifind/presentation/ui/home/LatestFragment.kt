package com.android.anifind.presentation.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.android.anifind.R
import com.android.anifind.databinding.FragmentLatestBinding
import com.android.anifind.extensions.init
import com.android.anifind.presentation.adapter.AdapterType.DEFAULT
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.BookmarksViewModel
import com.android.anifind.presentation.viewmodel.HomeViewModel

class LatestFragment : Fragment() {

    private val adapter = AnimePagingAdapter(DEFAULT)
    private val homeViewModel by activityViewModels<HomeViewModel>()
    private val bookmarksViewModel by activityViewModels<BookmarksViewModel>()
    private lateinit var binding: FragmentLatestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLatestBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.onItemClick = {
            bookmarksViewModel.setAnime(it)
            findNavController().navigate(R.id.action_fragmentHome_to_animeFragment)
        }
        binding.apply {
            recycler.init(adapter)
            btnRetry.setOnClickListener { adapter.retry() }
            adapter.addLoadStateListener {
                progressBar.isVisible =  it.source.refresh is LoadState.Loading
                recycler.isVisible =  it.source.refresh is LoadState.NotLoading
                errorMessage.isVisible = it.source.refresh is LoadState.Error
            }
        }
        homeViewModel.latest.observe(viewLifecycleOwner) { adapter.submitData(lifecycle, it) }

    }
}
