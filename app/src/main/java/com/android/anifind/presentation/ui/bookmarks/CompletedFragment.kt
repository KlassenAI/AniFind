package com.android.anifind.presentation.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentCompletedBinding
import com.android.anifind.extensions.conceal
import com.android.anifind.extensions.init
import com.android.anifind.presentation.adapter.AnimeAdapter
import com.android.anifind.presentation.viewmodel.BookmarksViewModel

class CompletedFragment : Fragment(R.layout.fragment_completed) {

    private val binding: FragmentCompletedBinding by viewBinding()
    private val viewModel: BookmarksViewModel by activityViewModels()
    private lateinit var adapter: AnimeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AnimeAdapter(viewModel, this@CompletedFragment)
        recycler.init(adapter)
        viewModel.completedAnimes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            progressBar.conceal()
            emptyMessage.isVisible = it.isEmpty()
            recycler.isVisible = it.isNotEmpty()
        }
    }
}