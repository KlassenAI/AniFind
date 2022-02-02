package com.android.anifind.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.AnimeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
open class SharedViewModel @Inject constructor(repository: Repository) : BaseViewModel(repository) {

    private val _homeAnime = MutableLiveData<Anime>()
    val homeAnime: LiveData<Anime> get() = _homeAnime
    fun initHomeAnime(anime: Anime) = _homeAnime.postValue(anime)

    private val _overviewAnime = MutableLiveData<Anime>()
    val overviewAnime: LiveData<Anime> get() = _overviewAnime
    fun initOverviewAnime(anime: Anime) = _overviewAnime.postValue(anime)

    private val _bookmarksAnime = MutableLiveData<AnimeEntity>()
    val bookmarksAnime: LiveData<AnimeEntity> get() = _bookmarksAnime
    fun initBookmarksAnime(animeEntity: AnimeEntity) = _bookmarksAnime.postValue(animeEntity)

    fun loadAnimeInfo(anime: AnimeEntity) {
        requestAnimeInfo(anime.id)
            .subscribeOn(Schedulers.io())
            .subscribe({ update(anime.apply { info = it }) }, {})
    }
}