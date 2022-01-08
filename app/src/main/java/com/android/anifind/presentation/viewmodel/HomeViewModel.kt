package com.android.anifind.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava3.cachedIn
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val isRequestMade = MutableLiveData(false)

    private val _latest = MutableLiveData<PagingData<Anime>>()
    val latest: LiveData<PagingData<Anime>> get() = _latest
    private val _latestState = MutableLiveData<LoadState>()
    val latestState: LiveData<LoadState> get() = _latestState

    private val _ongoings = MutableLiveData<PagingData<Anime>>()
    val ongoings: LiveData<PagingData<Anime>> get() = _ongoings
    private val _anons = MutableLiveData<PagingData<Anime>>()
    val anons: LiveData<PagingData<Anime>> get() = _anons

    private fun requestLatest() {
        repository.requestLatestAnimes().cachedIn(viewModelScope)
            .subscribe({ _latest.postValue(it) }, {})
    }

    private fun requestOnGoings() {
        repository.requestOnGoingAnimes().cachedIn(viewModelScope)
            .subscribe({ _ongoings.postValue(it) }, {})
    }

    private fun requestAnons() {
        repository.requestAnonsAnimes().cachedIn(viewModelScope)
            .subscribe({ _anons.postValue(it) }, {})
    }

    fun requestAll() {
        if (isRequestMade.value == false) {
            requestLatest()
            requestOnGoings()
            requestAnons()
            isRequestMade.postValue(true)
        }
    }
}
