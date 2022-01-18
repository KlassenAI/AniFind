package com.android.anifind.presentation.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentBookmarksBinding
import com.android.anifind.presentation.viewpager.ViewPagerAdapter
import com.android.anifind.presentation.viewpager.ViewPagerAdapter.Type.BOOKMARKS
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarksFragment : Fragment(R.layout.fragment_bookmarks) {

    private val titles = listOf("избранные", "смотрю", "запланировано", "просмотрено", "отложено", "брошено")
    private val binding: FragmentBookmarksBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle, BOOKMARKS)
        TabLayoutMediator(tabLayout, viewPager) { tab, p -> tab.text = titles[p] }.attach()
    }
}