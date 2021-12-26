package com.android.anifind.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _animes = MutableLiveData<List<Anime>>()
    val animes: LiveData<List<Anime>> = _animes

    var single = MutableLiveData<Observable<PagingData<Anime>>?>()

    fun searchAnimes(query: String) {
        single.postValue(repository.searchSingle(query).cachedIn(viewModelScope))
    }

    private fun log(text: String) {
        Log.d("SearchViewModel", text)
    }
}