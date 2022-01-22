package com.android.anifind.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.AnimeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val animes = repository.getAnimes()

    private val _anime = MutableLiveData<AnimeEntity>()
    val anime: LiveData<AnimeEntity> get() = _anime

    fun saveAnime(anime: AnimeEntity) = _anime.postValue(anime)

    fun requestAnimeInfo(id: Int) = repository.requestAnimeInfo(id)

    fun getAnime(id: Int) = repository.getAnime(id)
    fun insert(animeEntity: AnimeEntity) = repository.insert(animeEntity)
    fun update(animeEntity: AnimeEntity) = repository.update(animeEntity)
    fun delete(animeEntity: AnimeEntity) = repository.delete(animeEntity)
}