package com.android.anifind.presentation.ui.bookmarks

class WatchingFragment : SubBookmarksFragment() {

    override fun initData() = viewModel.watchingAnimes.observe()
}