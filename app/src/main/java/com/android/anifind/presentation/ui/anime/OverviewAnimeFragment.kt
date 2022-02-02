package com.android.anifind.presentation.ui.anime

class OverviewAnimeFragment : BaseAnimeFragment() {

    override fun initData() = viewModel.overviewAnime.observe()
}