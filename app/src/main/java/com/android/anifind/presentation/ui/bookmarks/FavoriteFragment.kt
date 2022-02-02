package com.android.anifind.presentation.ui.bookmarks

class FavoriteFragment : SubBookmarksFragment() {

    override fun initData() = viewModel.favoriteAnimes.observe()
}