package com.android.anifind.presentation.ui.anime

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentAnimeBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.extensions.navigateToRelated
import com.android.anifind.extensions.navigateToSimilar
import com.android.anifind.presentation.adapter.hide
import com.android.anifind.presentation.adapter.setImage
import com.android.anifind.presentation.viewmodel.SharedViewModel
import io.reactivex.rxjava3.schedulers.Schedulers

open class BaseAnimeFragment : Fragment(R.layout.fragment_anime) {

    val viewModel: SharedViewModel by activityViewModels()
    val binding: FragmentAnimeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        btnRelated.setOnClickListener { navigateToRelated() }
        btnSimilar.setOnClickListener { navigateToSimilar() }
    }

    fun bind(anime: Anime) = with(binding) {
        if (!anime.saved) {
            viewModel.insert(anime.entity)
            anime.saved = true
        }
        anime.entity.let {
            if (it.info == null) {
                viewModel.loadAnimeInfo(it)
                studio.hide()
                recycler.hide()
                description.hide()
            }
            poster.setImage(it.imageUrl)
            if (it.name == it.original) {
                russianTitle.text = it.name
                originalTitle.hide()
            } else {
                russianTitle.text = it.name
                originalTitle.text = it.original
            }
            date.text = it.date
            kind.text = it.kind
            episodes.text = it.episodesInfo
            score.text = it.score
        }
    }
}