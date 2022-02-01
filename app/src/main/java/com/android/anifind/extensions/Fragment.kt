package com.android.anifind.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.anifind.R
import com.google.android.material.snackbar.Snackbar

fun Fragment.navigateUp() = findNavController().navigateUp()
fun Fragment.navigateToBookmarksAnime() = findNavController().navigate(R.id.bookmarksAnimeFragment)
fun Fragment.navigateToOverviewAnime() = findNavController().navigate(R.id.overviewAnimeFragment)
fun Fragment.navigateToHomeAnime() = findNavController().navigate(R.id.homeAnimeFragment)
fun Fragment.navigateToSearch() = findNavController().navigate(R.id.searchFragment)
fun Fragment.navigateToFilter() = findNavController().navigate(R.id.filterFragment)
fun Fragment.navigateToSimilar() = findNavController().navigate(R.id.similarFragment)
fun Fragment.navigateToRelated() = findNavController().navigate(R.id.relatedFragment)
fun Fragment.navigateToRecent() = findNavController().navigate(R.id.recentFragment)
fun Fragment.showSnackbar(text: String, action: () -> Unit) {
    view?.let {
        Snackbar.make(it, text, Snackbar.LENGTH_SHORT)
            .setAction("Отмена") { action() }
            .show()
    }
}