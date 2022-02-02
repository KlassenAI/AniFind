package com.android.anifind.presentation.ui.home

class OngoingFragment : SubHomeFragment() {

    override fun initData() {
        viewModel.requestOnGoings()
        viewModel.ongoings.observe()
    }
}