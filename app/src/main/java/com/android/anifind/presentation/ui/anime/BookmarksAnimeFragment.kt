package com.android.anifind.presentation.ui.anime

class BookmarksAnimeFragment : BaseAnimeFragment() {

    override fun initData() = viewModel.bookmarksAnime.observe()
}