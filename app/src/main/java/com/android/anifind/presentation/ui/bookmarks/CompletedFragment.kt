package com.android.anifind.presentation.ui.bookmarks

class CompletedFragment : SubBookmarksFragment() {

    override fun initData() = viewModel.completedAnimes.observe()
}