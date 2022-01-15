package com.android.anifind.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.android.anifind.Constants.FILE_GENRES
import com.android.anifind.Constants.FILE_STUDIOS
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.Genre
import com.android.anifind.domain.model.Studio
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val repository: Repository,
    private val application: Application
) : BaseViewModel(repository) {

    private val isPopularRequestMade = MutableLiveData(false)
    private val _popularAnimes = MutableLiveData<PagingData<Anime>>()
    val popularAnimes: LiveData<PagingData<Anime>> get() = _popularAnimes

    fun requestPopularAnimes() {
        if (!isPopularRequestMade.value!!) {
            isPopularRequestMade.postValue(true)
            repository.requestPopularAnimes().cachedIn(viewModelScope)
                .subscribe({ _popularAnimes.postValue(it) }, {})
        }
    }

    private val _searchAnimes = MutableLiveData<PagingData<Anime>>()
    val searchAnimes: LiveData<PagingData<Anime>> get() = _searchAnimes

    fun requestAnimes(query: String) {
        repository.requestAnimes(query).cachedIn(viewModelScope)
            .subscribe({ _searchAnimes.postValue(it) }, {})
    }

    private val _recentRequests = MutableLiveData(arrayListOf<String>())
    val recentRequests: LiveData<ArrayList<String>> get() = _recentRequests

    fun saveRequest(query: String) {
        _recentRequests.value?.remove(query)
        _recentRequests.value?.add(query)
    }

    fun clearRecentRequests() {
        _recentRequests.postValue(arrayListOf())
    }

    private val _filterAnimes = MutableLiveData<PagingData<Anime>>()
    val filterAnimes: LiveData<PagingData<Anime>> get() = _filterAnimes
    private val _isFilterChanging = MutableLiveData(true)
    val isFilterChanging: LiveData<Boolean?> get() = _isFilterChanging

    fun requestFilterAnimes(map: HashMap<String, String>) {
        repository.requestFilterAnimes(map).cachedIn(viewModelScope)
            .subscribe({ _filterAnimes.postValue(it) }, {})
        _isFilterChanging.postValue(false)
    }

    fun setFilterChanging() {
        _isFilterChanging.postValue(true)
    }

    private val _genres = MutableLiveData(getGenres())
    val genres: LiveData<List<Genre>> get() = _genres
    private val _genreBooleans = MutableLiveData(BooleanArray(genreSize))
    val genreBooleans: LiveData<BooleanArray> = _genreBooleans
    private val genreSize: Int get() = getGenres().size

    fun saveGenres(booleanArray: BooleanArray) = _genreBooleans.postValue(booleanArray.copyOf())
    fun restoreGenres() = _genreBooleans.postValue(_genreBooleans.value?.copyOf())
    fun clearGenres() = _genreBooleans.postValue(BooleanArray(46))

    private fun getGenres(): List<Genre> {
        val genres: List<Genre> = Gson().fromJson(
            getJson(FILE_GENRES), object : TypeToken<List<Genre>>() {}.type
        )
        return genres.filter { it.kind == "anime" }.sortedBy { it.russian }
    }

    private val _studios = MutableLiveData(getStudios())
    val studios: LiveData<List<Studio>> get() = _studios
    private val _studiosBooleans = MutableLiveData(BooleanArray(studioSize))
    val studiosBooleans: LiveData<BooleanArray> = _studiosBooleans
    private val studioSize: Int get() = getStudios().size

    fun saveStudios(booleanArray: BooleanArray) = _studiosBooleans.postValue(booleanArray.copyOf())
    fun restoreStudios() = _studiosBooleans.postValue(_studiosBooleans.value?.copyOf())
    fun clearStudios() = _studiosBooleans.postValue(BooleanArray(441))

    private fun getStudios(): List<Studio> {
        val studios: List<Studio> = Gson().fromJson(
            getJson(FILE_STUDIOS), object : TypeToken<List<Studio>>() {}.type
        )
        return studios.filter { it.real }.sortedBy { it.name }
    }

    private fun getJson(fileName: String): String? {
        return application.assets?.open(fileName)?.bufferedReader().use { it?.readText() }
    }
}
