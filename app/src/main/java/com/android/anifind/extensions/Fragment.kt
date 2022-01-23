package com.android.anifind.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.anifind.R

fun Fragment.navigateUp() = findNavController().navigateUp()
fun Fragment.navigateToAnimeFragment() = findNavController().navigate(R.id.animeFragment)
fun Fragment.navigateToSearch() = findNavController().navigate(R.id.searchFragment)
fun Fragment.navigateToFilter() = findNavController().navigate(R.id.filterFragment)
fun Fragment.navigateToPopular() = findNavController().navigate(R.id.popularFragment)