package com.android.anifind.presentation.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.anifind.presentation.ui.home.AnonsFragment
import com.android.anifind.presentation.ui.home.LatestFragment
import com.android.anifind.presentation.ui.home.OnGoingFragment

class ViewPagerAdapter(
    manager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(manager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> OnGoingFragment()
            2 -> AnonsFragment()
            else -> LatestFragment()
        }
    }

    override fun getItemCount(): Int = 3
}