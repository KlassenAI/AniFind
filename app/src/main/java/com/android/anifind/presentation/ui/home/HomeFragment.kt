package com.android.anifind.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentHomeBinding
import com.android.anifind.presentation.adapter.viewpager.ViewPagerAdapter
import com.android.anifind.presentation.adapter.viewpager.ViewPagerAdapter.Type.HOME
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val titles = listOf("недавние", "онкоинги", "анонсы")
    private val binding: FragmentHomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle, HOME)
        TabLayoutMediator(tabLayout, viewPager) { tab, p -> tab.text = titles[p] }.attach()
    }
}