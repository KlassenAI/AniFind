package com.android.anifind.presentation.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.anifind.R
import com.android.anifind.databinding.FragmentBookmarksBinding
import com.android.anifind.extensions.init
import com.android.anifind.presentation.adapter.AnimeAdapter
import com.android.anifind.presentation.viewmodel.AnimeViewModel
import com.android.anifind.presentation.viewmodel.BookmarksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarksFragment : Fragment(R.layout.fragment_bookmarks) {

    private val adapter = AnimeAdapter()
    private val animeViewModel: AnimeViewModel by activityViewModels()
    private val bookmarksViewModel: BookmarksViewModel by activityViewModels()

    private lateinit var binding: FragmentBookmarksBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBookmarksBinding.bind(view)

        binding.apply { recycler.init(adapter) }

        bookmarksViewModel.animes.observe(viewLifecycleOwner, { adapter.animes = it })
    }
}