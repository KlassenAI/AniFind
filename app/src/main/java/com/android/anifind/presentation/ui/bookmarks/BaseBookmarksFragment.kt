package com.android.anifind.presentation.ui.bookmarks

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.anifind.domain.model.Anime
import com.android.anifind.extensions.navigateToBookmarksAnime
import com.android.anifind.extensions.showSnackbar
import com.android.anifind.presentation.adapter.AnimeAdapter
import com.android.anifind.presentation.viewmodel.SharedViewModel

open class BaseBookmarksFragment(@LayoutRes id: Int): Fragment(id), AnimeAdapter.OnItemClickListener {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun notifyItemClicked(anime: Anime) {
        sharedViewModel.initBookmarksAnime(anime)
        navigateToBookmarksAnime()
    }

    override fun notifyShowSnackbar(text: String, action: () -> Unit) = showSnackbar(text, action)
}