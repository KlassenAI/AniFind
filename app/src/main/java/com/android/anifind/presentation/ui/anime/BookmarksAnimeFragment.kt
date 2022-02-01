package com.android.anifind.presentation.ui.anime

import android.os.Bundle
import android.view.View
import com.android.anifind.extensions.navigateUp

class BookmarksAnimeFragment : BaseAnimeFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { navigateUp() }
        viewModel.bookmarksAnime.observe(viewLifecycleOwner) { anime -> bind(anime) }
    }
}