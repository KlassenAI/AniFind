package com.android.anifind.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var animes = MutableLiveData<Observable<PagingData<Anime>>?>()

    fun searchAnimes(query: String) {
        animes.postValue(repository.searchSingle(query).cachedIn(viewModelScope))
    }
}