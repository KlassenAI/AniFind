package com.android.anifind.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.android.anifind.Constants.DEFAULT_QUERY
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _animes = MutableLiveData<List<Anime>>()
    val animes: LiveData<List<Anime>> = _animes

    private val query = MutableLiveData(DEFAULT_QUERY)

    val single = query.switchMap {
        MutableLiveData(repository.searchSingle(it).cachedIn(viewModelScope))
    }

    fun searchAnimes(search: String) {
        query.postValue(search)
        /*
         repository.getAnimes(mapOf("search" to search, "limit" to "20"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    log("Subscribe")
                }
                .subscribe({
                    log("Success")
                    _animes.postValue(it)
                }, {
                    log("Error")
                }, {
                    // end
                    log("Complete")
                })
         */
    }

    private fun log(text: String) {
        Log.d("SearchViewModel", text)
    }
}