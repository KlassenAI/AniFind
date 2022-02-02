package com.android.anifind.presentation.ui.anime

class HomeAnimeFragment : BaseAnimeFragment() {

    override fun initData() = viewModel.homeAnime.observe()
}