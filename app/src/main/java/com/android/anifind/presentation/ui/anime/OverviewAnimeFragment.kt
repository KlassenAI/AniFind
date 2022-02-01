package com.android.anifind.presentation.ui.anime

import android.os.Bundle
import android.view.View
import com.android.anifind.extensions.navigateUp

class OverviewAnimeFragment : BaseAnimeFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { navigateUp() }
        viewModel.homeAnime.observe(viewLifecycleOwner) { anime -> bind(anime) }
    }
}