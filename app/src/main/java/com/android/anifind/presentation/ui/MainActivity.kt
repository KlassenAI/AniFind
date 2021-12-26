package com.android.anifind.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.android.anifind.R
import com.android.anifind.databinding.ActivityMainBinding
import com.android.anifind.domain.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var searchFragment: SearchFragment
    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var notificationsFragment: NotificationsFragment

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.nav_search -> {
                    binding.viewPager2.setCurrentItem(0, false)
                }
                R.id.nav_dashboard -> {
                    binding.viewPager2.setCurrentItem(1, false)
                }
                R.id.nav_notifications -> {
                    binding.viewPager2.setCurrentItem(2, false)
                }
            }
            return@setOnItemSelectedListener false
        }

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                when(position) {
                    0 -> { binding.navigation.menu.findItem(R.id.nav_search).isChecked = true; }
                    1 -> { binding.navigation.menu.findItem(R.id.nav_dashboard).isChecked = true; }
                    2 -> { binding.navigation.menu.findItem(R.id.nav_notifications).isChecked = true; }
                }
            }
        })

        setupViewPager(binding.viewPager2)
    }

    private fun setupViewPager(viewPager: ViewPager2) {
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        searchFragment = SearchFragment()
        dashboardFragment = DashboardFragment()
        notificationsFragment = NotificationsFragment()
        adapter.addFragment(searchFragment)
        adapter.addFragment(dashboardFragment)
        adapter.addFragment(notificationsFragment)
        viewPager.adapter = adapter
    }
}
