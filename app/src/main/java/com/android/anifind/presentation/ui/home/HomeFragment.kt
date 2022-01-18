package com.android.anifind.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentHomeBinding
import com.android.anifind.presentation.viewmodel.HomeViewModel
import com.android.anifind.presentation.viewpager.ViewPagerAdapter
import com.android.anifind.presentation.viewpager.ViewPagerAdapter.Type.HOME
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val titles = listOf("недавние", "онкоинги", "анонсы")
    private val binding: FragmentHomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle, HOME)
            TabLayoutMediator(tabLayout, viewPager) { tab, p -> tab.text = titles[p] }.attach()
        }
    }
}