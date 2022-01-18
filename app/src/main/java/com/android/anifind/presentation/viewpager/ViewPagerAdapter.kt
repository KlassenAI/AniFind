package com.android.anifind.presentation.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.anifind.presentation.ui.bookmarks.*
import com.android.anifind.presentation.ui.home.AnonsFragment
import com.android.anifind.presentation.ui.home.LatestFragment
import com.android.anifind.presentation.ui.home.OnGoingFragment
import com.android.anifind.presentation.viewpager.ViewPagerAdapter.Type.BOOKMARKS
import com.android.anifind.presentation.viewpager.ViewPagerAdapter.Type.HOME

class ViewPagerAdapter(
    manager: FragmentManager,
    lifecycle: Lifecycle,
    private val type: Type
) : FragmentStateAdapter(manager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when(type) {
            HOME -> when(position) {
                1 -> OnGoingFragment()
                2 -> AnonsFragment()
                else -> LatestFragment()
            }
            BOOKMARKS -> when(position) {
                1 -> WatchingFragment()
                2 -> PlannedFragment()
                3 -> CompletedFragment()
                4 -> HoldFragment()
                5 -> DroppedFragment()
                else -> FavoriteFragment()
            }
        }
    }

    override fun getItemCount(): Int = when(type) {
        HOME -> 3
        BOOKMARKS -> 6
    }

    enum class Type {
        HOME,
        BOOKMARKS
    }
}