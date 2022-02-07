package com.android.anifind.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.presentation.ui.anime.SingleAnimeType
import com.android.anifind.presentation.ui.anime.SingleAnimeType.*
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

    fun loadAnimeInfo(anime: AnimeEntity, type: SingleAnimeType) {
        requestAnimeInfo(anime.id)
            .subscribeOn(Schedulers.io())
            .subscribe({
                updateWithResult(anime.apply { info = it })
                    .subscribeOn(Schedulers.io())
                    .subscribe {
                        when(type) {
                            BOOKMARKS -> _bookmarksAnime.postValue(_bookmarksAnime.value)
                            HOME -> _homeAnime.postValue(_homeAnime.value)
                            OVERVIEW -> _overviewAnime.postValue(_overviewAnime.value)
                        }
                    }
            }, {})
    }
}