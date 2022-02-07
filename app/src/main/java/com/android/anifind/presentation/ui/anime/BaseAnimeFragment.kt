package com.android.anifind.presentation.ui.anime

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageButton
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentAnimeBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.domain.model.WatchStatus.*
import com.android.anifind.extensions.*
import com.android.anifind.presentation.adapter.*
import com.android.anifind.presentation.ui.anime.SingleAnimeType.*
import com.android.anifind.presentation.viewmodel.BookmarksViewModel
import com.android.anifind.presentation.viewmodel.HomeViewModel
import com.android.anifind.presentation.viewmodel.OverviewViewModel
import com.android.anifind.presentation.viewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.item_anime.*

abstract class BaseAnimeFragment(
    private val type: SingleAnimeType
) : Fragment(R.layout.fragment_anime) {

    private val genreAdapter = GenreAdapter()
    private val viewModel: SharedViewModel by activityViewModels()
    private val binding: FragmentAnimeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclers()
        initButtons()
        initObservers()
    }

    private fun initRecyclers() = with(binding) {
        recycler.initHorizontal(genreAdapter)
    }

    private fun initButtons() = with(binding) {
        btnBack.setOnClickListener { navigateUp() }
    }

    private fun initObservers() = when (type) {
        BOOKMARKS -> viewModel.bookmarksAnime.observe(viewLifecycleOwner) { bind(it) }
        HOME -> viewModel.homeAnime.observe(viewLifecycleOwner) { bind(it) }
        OVERVIEW -> viewModel.overviewAnime.observe(viewLifecycleOwner) { bind(it) }
    }

    private fun bind(anime: Anime) {
        if (anime.saved.not()) {
            viewModel.insert(anime.entity)
            anime.saved = true
        }
        bind(anime.entity)
    }

    private fun bind(animeEntity: AnimeEntity) = with(binding) {
        val infoIsNotNull = animeEntity.info != null
        studio.isVisible = infoIsNotNull
        recycler.isVisible = infoIsNotNull
        description.isVisible = infoIsNotNull
        if (infoIsNotNull) {
            animeEntity.info?.let {
                studio.text = it.studios.joinToString { studio1 -> studio1.name }
                description.text = it.description
                genreAdapter.submitList(it.genres)
            }
        } else {
            viewModel.loadAnimeInfo(animeEntity, type)
        }
        poster.setImage(animeEntity.imageUrl)
        if (animeEntity.isNameRussian) {
            russianTitle.text = animeEntity.name
            originalTitle.text = animeEntity.original
        } else {
            russianTitle.text = animeEntity.original
            originalTitle.hide()
        }
        date.text = animeEntity.date
        kind.text = animeEntity.kind
        episodes.text = animeEntity.episodesInfo
        score.text = animeEntity.score

        changeFavoriteButtonDraw(btnFavorite, animeEntity.isFavorite)
        btnFavorite.setOnClickListener {
            changeFavoriteParam(animeEntity)
            val message = getFavoriteChangeMessage(animeEntity.isFavorite)
            showSnackbar(message) { changeFavoriteParam(animeEntity) }
        }

        btnStatus.setText(animeEntity.watchStatus.title)
        btnStatus.editText?.setOnClickListener {
            val old = animeEntity.watchStatus
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(animeEntity.name)
                .setSingleChoiceItems(WatchStatus.titles(), old.ordinal) { dialog, which ->
                    if (which == old.ordinal) return@setSingleChoiceItems
                    changeStatusParam(animeEntity, WatchStatus.getItem(which))
                    val message = getStatusChangeMessage(old, animeEntity.watchStatus)
                    showSnackbar(message) { changeStatusParam(animeEntity, old) }
                    dialog.dismiss()
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }

    private fun changeFavoriteButtonDraw(btn: ImageButton, favorite: Boolean) = when (favorite) {
        true -> btn.setDraw(R.drawable.ic_bookmark_filled)
        false -> btn.setDraw(R.drawable.ic_bookmark_border)
    }

    private fun changeFavoriteParam(animeEntity: AnimeEntity) {
        animeEntity.isFavorite = !animeEntity.isFavorite
        changeFavoriteButtonDraw(binding.btnFavorite, animeEntity.isFavorite)
        viewModel.update(animeEntity)
    }

    private fun changeStatusParam(animeEntity: AnimeEntity, new: WatchStatus) {
        animeEntity.watchStatus = new
        binding.btnStatus.setText(animeEntity.watchStatus.title)
        viewModel.update(animeEntity)
    }
}