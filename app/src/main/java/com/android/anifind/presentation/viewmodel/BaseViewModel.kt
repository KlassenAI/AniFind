package com.android.anifind.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _anime = MutableLiveData<Anime>()
    val anime: LiveData<Anime> get() = _anime

    fun saveAnime(anime: Anime) = _anime.postValue(anime)

    fun getAnime(id: Int) = repository.getAnime(id)
    fun insert(anime: Anime) = repository.insert(anime)
    fun update(anime: Anime) = repository.update(anime)
    fun delete(anime: Anime) = repository.delete(anime)
}