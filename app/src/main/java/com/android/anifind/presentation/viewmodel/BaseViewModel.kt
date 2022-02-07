package com.android.anifind.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.AnimeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    fun requestAnimeInfo(id: Int) = repository.requestAnimeInfo(id)
    fun getAnime(id: Int) = repository.getAnime(id)
    fun insert(animeEntity: AnimeEntity) = repository.insert(animeEntity)
    fun update(animeEntity: AnimeEntity) = repository.update(animeEntity)
    fun delete(animeEntity: AnimeEntity) = repository.delete(animeEntity)
    fun updateWithResult(animeEntity: AnimeEntity) = repository.updateWithResult(animeEntity)
}