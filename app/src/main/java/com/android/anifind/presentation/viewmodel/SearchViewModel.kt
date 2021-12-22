package com.android.anifind.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _animes = MutableLiveData<List<Anime>>()
    val animes: LiveData<List<Anime>> = _animes

    fun searchAnimes() {
        repository.getAnimes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                log("Success")
                _animes.postValue(it)
            }, {
                log("Error")
            }, {
                // end
                log("Complete")
            }, {
                // start
                log("Subscribe")
            })
    }

    private fun log(text: String) {
        Log.d("SearchViewModel", text)
    }
}