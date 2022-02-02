package com.android.anifind.presentation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentSubHomeBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.extensions.init
import com.android.anifind.extensions.navigateToHomeAnime
import com.android.anifind.extensions.showSnackbar
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.HomeViewModel
import com.android.anifind.presentation.viewmodel.SharedViewModel

abstract class SubHomeFragment : Fragment(R.layout.fragment_sub_home),
    AnimePagingAdapter.OnItemClickListener {

    val viewModel: HomeViewModel by activityViewModels()
    private val binding: FragmentSubHomeBinding by viewBinding()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: AnimePagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AnimePagingAdapter(viewModel, this)
        with(binding) { recycler.init(adapter, progressBar, errorMessage) }
        binding.btnRetry.setOnClickListener { adapter.retry() }
        initData()
    }

    abstract fun initData()

    override fun notifyItemClicked(anime: Anime) {
        Log.d("anime", anime.saved.toString())
        sharedViewModel.initHomeAnime(anime)
        navigateToHomeAnime()
    }

    override fun notifyShowSnackbar(text: String, action: () -> Unit) = showSnackbar(text, action)

    fun LiveData<PagingData<Anime>>.observe() {
        observe(viewLifecycleOwner) { adapter.submitData(lifecycle, it) }
    }
}