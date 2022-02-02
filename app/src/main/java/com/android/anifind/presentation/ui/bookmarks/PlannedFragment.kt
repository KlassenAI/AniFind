package com.android.anifind.presentation.ui.bookmarks

class PlannedFragment : SubBookmarksFragment() {

    override fun initData() = viewModel.plannedAnimes.observe()
}