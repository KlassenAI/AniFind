package com.android.anifind.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.anifind.R
import com.google.android.material.snackbar.Snackbar

fun Fragment.toast(text: String) = Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
fun Fragment.navigateUp() = findNavController().navigateUp()
fun Fragment.navigateToBookmarksAnime() = findNavController().navigate(R.id.bookmarksAnimeFragment)
fun Fragment.navigateToOverviewAnime() = findNavController().navigate(R.id.overviewAnimeFragment)
fun Fragment.navigateToHomeAnime() = findNavController().navigate(R.id.homeAnimeFragment)
fun Fragment.navigateToSearch() = findNavController().navigate(R.id.searchFragment)
fun Fragment.navigateToFilter() = findNavController().navigate(R.id.filterFragment)
fun Fragment.navigateToRecent() = findNavController().navigate(R.id.recentFragment)
fun Fragment.showSnackbar(text: String, action: () -> Unit) {
    view?.let {
        Snackbar.make(it, text, Snackbar.LENGTH_SHORT)
            .setAction("Отмена") { action() }
            .show()
    }
}