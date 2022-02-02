package com.android.anifind.presentation.ui.bookmarks

class DroppedFragment : SubBookmarksFragment() {

    override fun initData() = viewModel.droppedAnimes.observe()
}