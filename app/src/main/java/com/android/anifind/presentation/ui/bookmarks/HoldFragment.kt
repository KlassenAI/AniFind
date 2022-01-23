package com.android.anifind.presentation.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentHoldBinding
import com.android.anifind.extensions.conceal
import com.android.anifind.extensions.init
import com.android.anifind.presentation.adapter.AnimeAdapter
import com.android.anifind.presentation.viewmodel.BookmarksViewModel

class HoldFragment : Fragment(R.layout.fragment_hold) {

    private val binding: FragmentHoldBinding by viewBinding()
    private val viewModel: BookmarksViewModel by activityViewModels()
    private lateinit var adapter: AnimeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AnimeAdapter(viewModel, this@HoldFragment)
        recycler.init(adapter)
        viewModel.holdAnimes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            progressBar.conceal()
            emptyMessage.isVisible = it.isEmpty()
            recycler.isVisible = it.isNotEmpty()
        }
    }
}