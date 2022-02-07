package com.android.anifind.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel(repository) {

    private val isLatestRequested = MutableLiveData(false)
    private val _latest = MutableLiveData<PagingData<Anime>>()
    val latest: LiveData<PagingData<Anime>> get() = _latest
    private val isOnGoingsRequested = MutableLiveData(false)
    private val _ongoings = MutableLiveData<PagingData<Anime>>()
    val ongoings: LiveData<PagingData<Anime>> get() = _ongoings
    private val isAnonsRequested = MutableLiveData(false)
    private val _anons = MutableLiveData<PagingData<Anime>>()
    val anons: LiveData<PagingData<Anime>> get() = _anons

    fun requestLatest() {
        if (isLatestRequested.value == false) {
            repository.requestLatestAnimes().cachedIn(viewModelScope)
                .subscribe({ _latest.postValue(it) }, {})
            isLatestRequested.postValue(true)
        }
    }

    fun requestOngoings() {
        if (isOnGoingsRequested.value == false) {
            repository.requestOngoingAnimes().cachedIn(viewModelScope)
                .subscribe({ _ongoings.postValue(it) }, {})
            isOnGoingsRequested.postValue(true)
        }
    }

    fun requestAnons() {
        if (isAnonsRequested.value == false) {
            repository.requestAnonsAnimes().cachedIn(viewModelScope)
                .subscribe({ _anons.postValue(it) }, {})
            isAnonsRequested.postValue(true)
        }
    }
}