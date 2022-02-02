package com.android.anifind.presentation.ui.anime

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentAnimeBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.domain.model.WatchStatus.*
import com.android.anifind.extensions.*
import com.android.anifind.presentation.adapter.*
import com.android.anifind.presentation.viewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BaseAnimeFragment : Fragment(R.layout.fragment_anime) {

    val viewModel: SharedViewModel by activityViewModels()
    private val binding: FragmentAnimeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        btnBack.setOnClickListener { navigateUp() }
        btnRelated.setOnClickListener { navigateToRelated() }
        btnSimilar.setOnClickListener { navigateToSimilar() }
        initData()
    }

    abstract fun initData()

    fun LiveData<AnimeEntity>.observe() = observe(viewLifecycleOwner) { bind(it) }

    @JvmName("observeAnime")
    fun LiveData<Anime>.observe() = observe(viewLifecycleOwner) {
        if (it.saved.not()) {
            viewModel.insert(it.entity)
            it.saved = true
        }
        bind(it.entity)
    }

    private fun bind(animeEntity: AnimeEntity) = with(binding) {
        if (animeEntity.info == null) {
            viewModel.loadAnimeInfo(animeEntity)
            studio.hide()
            recycler.hide()
            description.hide()
        }
        poster.setImage(animeEntity.imageUrl)
        if (animeEntity.name == animeEntity.original) {
            russianTitle.text = animeEntity.name
            originalTitle.hide()
        } else {
            russianTitle.text = animeEntity.name
            originalTitle.text = animeEntity.original
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

    private fun changeFavoriteButtonDraw(btn: ImageButton, favorite: Boolean) = when(favorite) {
        true -> btn.setDraw(R.drawable.ic_bookmark_filled_36)
        false -> btn.setDraw(R.drawable.ic_bookmark_border_36)
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