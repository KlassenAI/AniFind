package com.android.anifind.presentation.ui.bookmarks

class HoldFragment : SubBookmarksFragment() {

    override fun initData() = viewModel.holdAnimes.observe()
}