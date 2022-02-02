package com.android.anifind.presentation.ui.home

class AnonsFragment : SubHomeFragment() {

    override fun initData() {
        viewModel.requestAnons()
        viewModel.anons.observe()
    }
}