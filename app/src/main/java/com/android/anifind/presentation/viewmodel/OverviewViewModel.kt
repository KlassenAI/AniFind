package com.android.anifind.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val repository: Repository,
    private val application: Application
) : ViewModel() {

    private val _recentRequests = MutableLiveData(arrayListOf<String>())
    val recentRequests: LiveData<ArrayList<String>> = _recentRequests
    private val _searchAnimes = MutableLiveData<PagingData<Anime>>()
    val searchAnimes: LiveData<PagingData<Anime>> = _searchAnimes

    fun requestAnimes(query: String) {
        repository.requestAnimes(query).cachedIn(viewModelScope)
            .subscribe({ _searchAnimes.postValue(it) }, {})
    }

    fun saveRequest(query: String) {
        _recentRequests.value?.remove(query)
        _recentRequests.value?.add(query)
    }

    fun clearRecentRequests() {
        _recentRequests.postValue(arrayListOf())
    }

    private val _filterAnimes = MutableLiveData<Observable<PagingData<Anime>>?>()
    val filterAnimes: LiveData<Observable<PagingData<Anime>>?> = _filterAnimes
    private val _isFilterChanging = MutableLiveData(true)
    val isFilterChanging: LiveData<Boolean?> = _isFilterChanging

    fun requestFilterAnimes(map: HashMap<String, String>) {
        _filterAnimes.postValue(repository.requestFilterAnimes(map))
        _isFilterChanging.postValue(false)
    }

    fun setFilterChanging() {
        _isFilterChanging.postValue(true)
    }

    private val _genres = MutableLiveData(getGenreList())
    val genres: LiveData<List<Genre>> = _genres

    private val _genreBooleans = MutableLiveData(BooleanArray(46))
    val genreBooleans: LiveData<BooleanArray> = _genreBooleans

    fun saveGenres(booleanArray: BooleanArray) {
        _genreBooleans.postValue(booleanArray.copyOf())
    }

    fun restoreGenres() {
        _genreBooleans.postValue(_genreBooleans.value?.copyOf())
    }

    fun clearGenres() {
        _genreBooleans.postValue(BooleanArray(46))
    }

    private fun getGenreList(): List<Genre> {
        var genres: List<Genre> = Gson().fromJson(
            application.assets?.open("genres.json")?.bufferedReader().use { it?.readText() },
            object : TypeToken<List<Genre>>() {}.type
        )
        genres = genres.filter { it.kind == "anime" }.sortedBy { it.russian }
        return genres
    }
}
