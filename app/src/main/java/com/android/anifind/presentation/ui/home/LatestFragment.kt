package com.android.anifind.presentation.ui.home

class LatestFragment : SubHomeFragment() {

    override fun initData() {
        viewModel.requestLatest()
        viewModel.latest.observe()
    }
}