package com.android.anifind.presentation.ui.home

import android.util.Log
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.anifind.domain.model.Anime
import com.android.anifind.extensions.navigateToHomeAnime
import com.android.anifind.extensions.showSnackbar
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.SharedViewModel

open class BaseHomeFragment(@LayoutRes id: Int) : Fragment(id), AnimePagingAdapter.OnItemClickListener {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun notifyItemClicked(anime: Anime) {
        Log.d("anime", anime.saved.toString())
        if (!anime.saved) {
            sharedViewModel.initHomeAnime(anime)
            navigateToHomeAnime()
        }
    }

    override fun notifyShowSnackbar(text: String, action: () -> Unit) = showSnackbar(text, action)
}